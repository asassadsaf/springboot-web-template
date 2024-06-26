package com.fkp.template.modules.dbintegrity.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fkp.template.core.cache.DatabaseIntegrityDataSourceCache;
import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.dto.PageDTO;
import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.core.util.MybatisPlusUtils;
import com.fkp.template.core.util.SpringBeanUtils;
import com.fkp.template.modules.dbintegrity.constant.VerifyStatusEnum;
import com.fkp.template.modules.dbintegrity.context.DatabaseTypeContext;
import com.fkp.template.modules.dbintegrity.entity.DatabaseIntegrity;
import com.fkp.template.modules.dbintegrity.entity.DatabaseQueryVo;
import com.fkp.template.modules.dbintegrity.entity.KeyInfoVo;
import com.fkp.template.modules.dbintegrity.entity.QueryDoHmacVo;
import com.fkp.template.modules.dbintegrity.mapper.DatabaseIntegrityMapper;
import com.fkp.template.modules.dbintegrity.params.request.DatabaseIntegrityAddRequest;
import com.fkp.template.modules.dbintegrity.params.request.DatabaseIntegrityPageRequest;
import com.fkp.template.modules.dbintegrity.scheduler.IntegrityVerifyTask;
import com.fkp.template.modules.dbintegrity.scheduler.IntegrityVerifyTaskTest;
import com.fkp.template.modules.dbintegrity.service.DatabaseIntegrityService;
import com.fkp.template.modules.dbintegrity.service.DatabaseTypeService;
import com.fkp.template.modules.dbintegrity.util.ParamsConvertUtils;
import com.fkp.template.modules.dbintegrity.util.PwdUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.security.Key;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 14:37
 */
@Service
@Slf4j
public class DatabaseIntegrityServiceImpl implements DatabaseIntegrityService {

    @Autowired
    private DatabaseIntegrityMapper databaseIntegrityMapper;

    @Autowired
    private DatabaseTypeContext context;

    @Value("${business.databaseIntegrity.connectTimeOut:5000}")
    private Long connectTimeOut;

    @Value("${business.databaseIntegrity.minEvictableIdleTimeMillis:30000}")
    private Long minEvictableIdleTimeMillis;

    @Autowired
    private DatabaseIntegrityDataSourceCache dataSourceCache;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    private static final ConcurrentMap<String, ScheduledFuture<?>> SCHEDULER_MAP = new ConcurrentHashMap<>();


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestSimpleResponse<?> addIntegrity(DatabaseIntegrityAddRequest params) {
        DatabaseIntegrity databaseIntegrity = DatabaseIntegrity.instance(params);
        Long count = queryCountByUnique(databaseIntegrity);
        if(count != 0){
            params.setDbPwd(null);
            log.error("Add database integrity already exist. count: {}, request: {}", count, params);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_INFO_ALREADY_EXIST_ERROR);
        }
        KeyInfoVo keyInfoVo = getKeyInfoByName(databaseIntegrity.getKeyName(), databaseIntegrity.getKeyAlg());
        DatabaseQueryVo databaseQueryVo = convertDatabaseIntegrity2DatabaseQueryVo(databaseIntegrity, false);
        QueryDoHmacVo queryDoHmacVo = validateConnectAndDoHmac(databaseQueryVo, keyInfoVo.getMaterial(), keyInfoVo.getAlg());
        Long rowCount = queryDoHmacVo.getRowCount();
        Integer realEndLimit = ParamsConvertUtils.convertRealEndLimit(databaseIntegrity.getEndLimit(), rowCount, databaseIntegrity.getStartLimit());
        databaseIntegrity.setEndLimit(realEndLimit);
        databaseIntegrity.setHmacHex(queryDoHmacVo.getHmacHex());
        databaseIntegrity.setVerifyStatus(VerifyStatusEnum.NOT_VERIFY.getValue());
        databaseIntegrity.setKeyId(keyInfoVo.getKeyId());
        databaseIntegrity.setDbPwd(PwdUtils.encPwd(databaseIntegrity.getDbPwd()));
        save(databaseIntegrity);
        return RestSimpleResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestSimpleResponse<?> deleteIntegrity(String id) {
        DatabaseIntegrity databaseIntegrity = databaseIntegrityMapper.selectById(id);
        if(databaseIntegrity == null){
            log.error("Delete database integrity select by id not exist. id: {}", id);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_INFO_NOT_EXIST_ERROR);
        }
        DatabaseTypeService databaseTypeService = context.of(databaseIntegrity.getDbType());
        String url = databaseTypeService.generateUrl(databaseIntegrity.getDbIp(), databaseIntegrity.getDbPort(), databaseIntegrity.getDbCase(), databaseIntegrity.getDbSchema());
        dataSourceCache.invalidate(url);
        int res = databaseIntegrityMapper.deleteById(id);
        if(res == 0){
            log.error("Delete database integrity database error. id: {}", id);
            throw new RestBusinessException(RestErrorEnum.DATABASE_EXCEPTION);
        }
        return RestSimpleResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestSimpleResponse<?> verify(String id) {
        DatabaseIntegrity databaseIntegrity = databaseIntegrityMapper.selectById(id);
        if(databaseIntegrity == null){
            log.error("Verify database integrity select info from database not exist. id: {}", id);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_INFO_NOT_EXIST_ERROR);
        }
        KeyInfoVo keyInfoVo = getKeyInfoByName(databaseIntegrity.getKeyName(), databaseIntegrity.getKeyAlg());
        QueryDoHmacVo queryDoHmacVo = null;
        try {
            DatabaseQueryVo databaseQueryVo = convertDatabaseIntegrity2DatabaseQueryVo(databaseIntegrity, true);
            queryDoHmacVo = validateConnectAndDoHmac(databaseQueryVo, keyInfoVo.getMaterial(), keyInfoVo.getAlg());
        }catch (Exception e){
            log.error("Verify database integrity during queryDoHmac exception, set status is error.", e);
        }
        VerifyStatusEnum verifyStatusEnum;
        if(queryDoHmacVo == null || StringUtils.isBlank(queryDoHmacVo.getHmacHex())){
            verifyStatusEnum = VerifyStatusEnum.ERROR;
        }else if (!queryDoHmacVo.getHmacHex().equals(databaseIntegrity.getHmacHex())) {
            verifyStatusEnum = VerifyStatusEnum.FAIL;
        }else {
            verifyStatusEnum = VerifyStatusEnum.SUCCESS;
        }
        updateVerifyStatusById(id, verifyStatusEnum);
        return RestSimpleResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestSimpleResponse<?> refresh(String id) {
        DatabaseIntegrity databaseIntegrity = databaseIntegrityMapper.selectById(id);
        if(databaseIntegrity == null){
            log.error("Refresh database integrity select info from database not exist. id: {}", id);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_INFO_NOT_EXIST_ERROR);
        }
        boolean success = true;
        try {
            DatabaseQueryVo databaseQueryVo = convertDatabaseIntegrity2DatabaseQueryVo(databaseIntegrity, true);
            validateConnect(databaseQueryVo);
        }catch (Exception e){
            success = false;
            log.error("Refresh database integrity validateConnect exception, set verifyStatus error status.", e);
        }
        Integer verifyStatus = databaseIntegrity.getVerifyStatus();
        VerifyStatusEnum verifyStatusEnum = null;
        if(success && VerifyStatusEnum.ERROR.getValue().equals(verifyStatus)){
            verifyStatusEnum = VerifyStatusEnum.NOT_VERIFY;
        }else if(!success && !VerifyStatusEnum.ERROR.getValue().equals(verifyStatus)){
            verifyStatusEnum = VerifyStatusEnum.ERROR;
        }
        if(verifyStatusEnum != null){
            updateVerifyStatusById(id, verifyStatusEnum);
        }
        return RestSimpleResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestSimpleResponse<?> update(String id) {
        DatabaseIntegrity databaseIntegrity = databaseIntegrityMapper.selectById(id);
        if(databaseIntegrity == null){
            log.error("Update database integrity select info from database not exist. id: {}", id);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_INFO_NOT_EXIST_ERROR);
        }
        DatabaseQueryVo databaseQueryVo = convertDatabaseIntegrity2DatabaseQueryVo(databaseIntegrity, true);
        KeyInfoVo keyInfoVo = getKeyInfoByName(databaseIntegrity.getKeyName(), databaseIntegrity.getKeyAlg());
        QueryDoHmacVo queryDoHmacVo = validateConnectAndDoHmac(databaseQueryVo, keyInfoVo.getMaterial(), keyInfoVo.getAlg());
        Integer realEndLimit = ParamsConvertUtils.convertRealEndLimit(databaseIntegrity.getEndLimit(), queryDoHmacVo.getRowCount(), databaseIntegrity.getStartLimit());
        int update = databaseIntegrityMapper.update(null, Wrappers.lambdaUpdate(DatabaseIntegrity.class)
                .set(DatabaseIntegrity::getHmacHex, queryDoHmacVo.getHmacHex())
                .set(DatabaseIntegrity::getEndLimit, realEndLimit).set(DatabaseIntegrity::getVerifyStatus, VerifyStatusEnum.NOT_VERIFY.getValue())
                .eq(DatabaseIntegrity::getId, id));
        if(update == 0){
            log.error("Update database integrity verify database error. id: {}, hmacHex: {}, realEndLimit: {}", id, queryDoHmacVo.getHmacHex(), realEndLimit);
            throw new RestBusinessException(RestErrorEnum.DATABASE_EXCEPTION);
        }
        return RestSimpleResponse.success();
    }

    @Override
    public RestSimpleResponse<PageDTO<DatabaseIntegrity>> page(DatabaseIntegrityPageRequest params) {
        Page<DatabaseIntegrity> page = new Page<>();
        page.setCurrent(params.getPageNum());
        page.setSize(params.getPageSize());
        page.addOrder(new OrderItem().setColumn(MybatisPlusUtils.getColumnNameByRef(DatabaseIntegrity::getId)).setAsc(params.getAsc()));
        Page<DatabaseIntegrity> pageRes = databaseIntegrityMapper.selectPage(page, Wrappers.lambdaQuery(DatabaseIntegrity.class)
                .eq(StringUtils.isNotBlank(params.getDbType()), DatabaseIntegrity::getDbType, params.getDbType())
                .like(StringUtils.isNotBlank(params.getDbIp()), DatabaseIntegrity::getDbIp, params.getDbIp())
                .like(StringUtils.isNotBlank(params.getDbCase()), DatabaseIntegrity::getDbCase, params.getDbCase())
                .like(StringUtils.isNotBlank(params.getDbSchema()), DatabaseIntegrity::getDbSchema, params.getDbSchema())
                .like(StringUtils.isNotBlank(params.getDbTable()), DatabaseIntegrity::getDbTable, params.getDbTable())
                .like(StringUtils.isNotBlank(params.getKeyName()), DatabaseIntegrity::getKeyName, params.getKeyName()));
        PageDTO<DatabaseIntegrity> kmsPageDTO = new PageDTO<>();
        kmsPageDTO.setPageNum(Long.valueOf(pageRes.getCurrent()).intValue());
        kmsPageDTO.setPageSize(Long.valueOf(pageRes.getSize()).intValue());
        kmsPageDTO.setTotal(pageRes.getTotal());
        kmsPageDTO.setList(pageRes.getRecords());
        return RestSimpleResponse.success(kmsPageDTO);
    }

    @Override
    public Long queryCountByUnique(DatabaseIntegrity params){
        return databaseIntegrityMapper.selectCount(Wrappers.lambdaQuery(DatabaseIntegrity.class).eq(DatabaseIntegrity::getDbTable, params.getDbTable())
                .eq(DatabaseIntegrity::getDbIp, params.getDbIp()).eq(DatabaseIntegrity::getDbPort, params.getDbPort())
                .eq(DatabaseIntegrity::getDbUser, params.getDbUser()).eq(DatabaseIntegrity::getDbSchema, params.getDbSchema())
                .eq(DatabaseIntegrity::getDbCase, params.getDbCase()).eq(DatabaseIntegrity::getDbTable, params.getDbTable())
                .eq(DatabaseIntegrity::getDbSql, params.getDbSql()).eq(DatabaseIntegrity::getStartLimit, params.getStartLimit())
                .eq(DatabaseIntegrity::getEndLimit, params.getEndLimit()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DatabaseIntegrity databaseIntegrity){
        try {
            databaseIntegrityMapper.insert(databaseIntegrity);
        }catch (Exception e){
            log.error("Save database integrity database error. insertData: {}", databaseIntegrity);
            throw new RestBusinessException(RestErrorEnum.DATABASE_EXCEPTION, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVerifyStatusById(String id, VerifyStatusEnum verifyStatusEnum){
        int update = databaseIntegrityMapper.update(null, Wrappers.lambdaUpdate(DatabaseIntegrity.class)
                .set(DatabaseIntegrity::getVerifyStatus, verifyStatusEnum.getValue())
                .eq(DatabaseIntegrity::getId, id));
        if(update == 0){
            log.error("Update verifyStatus database error. id: {}, verifyStatus: {}", id, verifyStatusEnum);
            throw new RestBusinessException(RestErrorEnum.DATABASE_EXCEPTION);
        }
    }
    @Override
    public List<DatabaseIntegrity> listIdAndFrequency(){
        return databaseIntegrityMapper.selectList(Wrappers.lambdaQuery(DatabaseIntegrity.class)
                .select(DatabaseIntegrity::getId, DatabaseIntegrity::getFrequency));
    }

    @Override
    public synchronized void startVerifyIntegrityScheduler(String id, Integer frequency){
        boolean exist = SCHEDULER_MAP.get(id) != null;
        if(!exist){
            IntegrityVerifyTask runnable = SpringBeanUtils.getBean(IntegrityVerifyTask.class);
            runnable.setId(id);
            runnable.setFrequency(frequency);
            runnable.setSchedulerMap(SCHEDULER_MAP);
            ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(runnable,
                    Instant.ofEpochMilli(System.currentTimeMillis() + 1000L * 60 * frequency), Duration.ofMinutes(frequency));
            SCHEDULER_MAP.put(id, scheduledFuture);
        }
    }

    // ----------测试手动提交事务start-------------
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    private synchronized void startVerifyIntegritySchedulerTest(String id, Integer frequency){
        boolean exist = SCHEDULER_MAP.get(id) != null;
        if(!exist){
            IntegrityVerifyTaskTest runnable = new IntegrityVerifyTaskTest();
            runnable.setId(id);
            runnable.setFrequency(frequency);
            runnable.setSchedulerMap(SCHEDULER_MAP);
            runnable.setService(this);
            runnable.setTransactionManager(transactionManager);
            runnable.setTransactionDefinition(transactionDefinition);
            ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(runnable,
                    Instant.ofEpochMilli(System.currentTimeMillis() + 1000L * 60 * frequency), Duration.ofMinutes(frequency));
            SCHEDULER_MAP.put(id, scheduledFuture);
        }
    }
    // ----------测试手动提交事务end-------------

    private QueryDoHmacVo validateConnectAndDoHmac(DatabaseQueryVo databaseQueryVo, String keyMaterial, String keyAlg){
        DataSource dataSource = getDataSource(databaseQueryVo.getUrl(), databaseQueryVo.getDriverClassName(), databaseQueryVo.getUsername(), databaseQueryVo.getPassword());
        try (Connection connection = getConnection(dataSource)){
            validateSql(connection, databaseQueryVo.getExecSql());
            return queryDataAndDoHmac(connection, databaseQueryVo.getExecSql(), keyMaterial, ParamsConvertUtils.convertKeyAlg(keyAlg));
        }catch (RestBusinessException e){
            throw e;
        }catch (Exception e){
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DO_HMAC_ERROR, e);
        }
    }

    private void validateConnect(DatabaseQueryVo databaseQueryVo){
        DataSource dataSource = getDataSource(databaseQueryVo.getUrl(), databaseQueryVo.getDriverClassName(), databaseQueryVo.getUsername(), databaseQueryVo.getPassword());
        try (Connection connection = getConnection(dataSource)){
            validateSql(connection, databaseQueryVo.getExecSql());
        }catch (RestBusinessException e){
            throw e;
        }catch (Exception e){
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DO_HMAC_ERROR, e);
        }
    }

    private DatabaseQueryVo convertDatabaseIntegrity2DatabaseQueryVo(DatabaseIntegrity params, boolean isEncPwd){
        DatabaseTypeService databaseTypeService = context.of(params.getDbType());
        String driverClassName = databaseTypeService.generateDriverClassName();
        String url = databaseTypeService.generateUrl(params.getDbIp(), params.getDbPort(), params.getDbCase(), params.getDbSchema());
        Integer limitCount = ParamsConvertUtils.convertLimitCount(params.getStartLimit(), params.getEndLimit());
        String execSql = databaseTypeService.generateSql(params.getDbSql(), ParamsConvertUtils.convertStartLimitIndex(params.getStartLimit()), limitCount);
        String dbPwd = params.getDbPwd();
        if(isEncPwd){
            dbPwd = PwdUtils.decPwd(dbPwd);
        }
        return DatabaseQueryVo.builder().url(url).driverClassName(driverClassName).username(params.getDbUser()).password(dbPwd).execSql(execSql).build();
    }



    private KeyInfoVo getKeyInfoByName(String keyName, String keyAlg) {
        //返回固定密钥信息
        return KeyInfoVo.builder().keyId("Symme58eef3f-5f37-4f1c-bf76-0350a1174ace").material("jGL0p/PXATUaTlmm+yrT8/hkeqMi1OVyT8Ab9gFb+Q4=").alg("HMAC_SM3").build();
    }

    private QueryDoHmacVo queryDataAndDoHmac(Connection connection, String execSql, String material, String alg) {
        try (Statement statement = connection.createStatement()){
            statement.setFetchSize(1024);
            if(!statement.execute(execSql)){
                log.error("Validate exec sql error. sql: {}", execSql);
                throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_EXECUTE_SQL_ERROR);
            }
            try (ResultSet resultSet = statement.getResultSet()){
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                long rowCount = 0L;
                Key key = new SecretKeySpec(Base64.decodeBase64(material), alg);
                Mac mac = Mac.getInstance(alg, BouncyCastleProvider.PROVIDER_NAME);
                mac.init(key);
                while (resultSet.next()){
                    rowCount++;
                    for (int i = 1; i <= columnCount; i++) {
                        byte[] bytes = resultSet.getBytes(i);
                        if(bytes != null){
                            mac.update(bytes);
                        }
                    }
                }
                return QueryDoHmacVo.builder().hmacHex(Hex.encodeHexString(mac.doFinal())).rowCount(rowCount).build();
            }
        }catch (RestBusinessException e){
            throw e;
        } catch (Exception e){
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DO_HMAC_ERROR, e);
        }
    }

    private void validateSql(Connection connection, String execSql) {
        try (Statement statement = connection.createStatement()){
            statement.setMaxRows(1);
            if(!statement.execute(execSql)){
                log.error("Validate exec sql error. sql: {}", execSql);
                throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_EXECUTE_SQL_ERROR);
            }
            try (ResultSet resultSet = statement.getResultSet()){
                if(!resultSet.next()){
                    log.error("Validate exec sql result is blank. sql: {}", execSql);
                    throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_EXECUTE_SQL_NO_DATA_ERROR);
                }
            }
        }catch (RestBusinessException e){
            throw e;
        } catch (Exception e){
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_EXECUTE_SQL_ERROR, e);
        }
    }

    private Connection getConnection(DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            return connection;
        }catch (Exception e){
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error("Close connection error.", ex);
                }
            }
            if(dataSource instanceof DruidDataSource){
                dataSourceCache.invalidate(((DruidDataSource) dataSource).getUrl());
            }
            log.error("Get remote database connect error.");
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_CONNECT_REMOTE_DB_ERROR, e);
        }
    }

    private DataSource getDataSource(String url, String driverClassName, String username, String password){
        DataSource dataSource = dataSourceCache.getIfPresent(url);
        if(dataSource == null){
            synchronized (DatabaseIntegrityServiceImpl.class){
                dataSource = dataSourceCache.getIfPresent(url);
                if(dataSource == null){
                    DruidDataSource druidDataSource = new DruidDataSource();
                    druidDataSource.setUrl(url);
                    druidDataSource.setDriverClassName(driverClassName);
                    druidDataSource.setUsername(username);
                    druidDataSource.setPassword(password);
                    druidDataSource.setMaxActive(4);
                    druidDataSource.setMaxWait(connectTimeOut);
                    druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
                    druidDataSource.setTestWhileIdle(false);
                    dataSourceCache.put(url, druidDataSource);
                    dataSource = druidDataSource;
                }
            }
        }
        return dataSource;
    }



}
