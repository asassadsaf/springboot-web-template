package com.fkp.template;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fkp.template.core.util.StrIdGenerator;
import com.fkp.template.modules.app.entity.SysApp;
import com.fkp.template.modules.app.mapper.SysAppMapper;
import com.fkp.template.modules.dbintegrity.entity.DatabaseIntegrity;
import com.fkp.template.modules.dbintegrity.mapper.DatabaseIntegrityMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/21 11:04
 */
@SpringBootTest
@Slf4j
public class MybatisPlusTest {

    @Autowired
    private DatabaseIntegrityMapper databaseIntegrityMapper;

    @Autowired
    private SysAppMapper sysAppMapper;

    @Test
    void testSysApp(){
        int insert = sysAppMapper.insert(SysApp.builder().name("fkp2").age(25).addr("jinan").build());
        log.info("insert sys app res: {}", insert);
        List<SysApp> sysApps = sysAppMapper.selectList(Wrappers.emptyWrapper());
        log.info("select sys_app table list res: {}", sysApps);
    }

    @Test
    void testMybatisPlusIdGenerator(){
        Sequence sequence = new Sequence(null);
        System.out.println(String.valueOf(sequence.nextId()));
        System.out.println(String.valueOf(sequence.nextId()).length());
    }

    @Test
    void testSelect(){
        List<DatabaseIntegrity> databaseIntegrities = databaseIntegrityMapper.selectList(Wrappers.emptyWrapper());
        System.out.println(databaseIntegrities);
    }

    @Test
    void testSave(){
        List<DatabaseIntegrity> databaseIntegrities = databaseIntegrityMapper.selectList(Wrappers.emptyWrapper());
        DatabaseIntegrity databaseIntegrity = databaseIntegrities.get(0);
        log.info("select index 0 result: {}", databaseIntegrity);
        databaseIntegrity.setId(null);
        databaseIntegrity.setCreateDate(null);
        databaseIntegrity.setUpdateDate(null);
        log.info("set id,createDate,updateDate is null. result: {}", databaseIntegrity);
        int insert = databaseIntegrityMapper.insert(databaseIntegrity);
        log.info("insert entity. res: {}, entity: {}", insert, databaseIntegrity);
        DatabaseIntegrity databaseIntegrity1 = databaseIntegrityMapper.selectOne(Wrappers.lambdaQuery(DatabaseIntegrity.class).eq(DatabaseIntegrity::getId, databaseIntegrity.getId()));
        log.info("select insert data. res: {}", databaseIntegrity1);
    }

    @Test
    void testSelectMapList() {
//        System.out.println(sysAppMapper.selectList(Wrappers.emptyWrapper()));
        try {
            QueryWrapper<SysApp> wrapper = Wrappers.query(new SysApp()).select("id", "age").eq("name", "fkp");
            List<Map<String, Object>> maps = sysAppMapper.selectMaps(wrapper);
            System.out.println(maps);
        } catch (BadSqlGrammarException e) {
            String invalidAttrName = getInvalidAttrName(e);
            throw new RuntimeException(invalidAttrName);
        }
    }

    /**
     * 验证数据库唯一索引或主键冲突引发的异常类型
     */
    @Test
    void testUniqueIndex(){
        SysApp sysApp = SysApp.builder().id("003").name("fkpfkpfkp").age(24).addr("jinan").remark("ABC").build();
        try {
            int insert = sysAppMapper.insert(sysApp);
        }catch (Exception e){
            if(e instanceof DuplicateKeyException || e.getCause() instanceof SQLIntegrityConstraintViolationException || e instanceof DataIntegrityViolationException){
                throw new RuntimeException("sysApp already exist.", e);
            }else {
                throw new RuntimeException("unknown error", e);
            }
        }
        List<SysApp> sysApps = sysAppMapper.selectList(Wrappers.lambdaQuery(SysApp.class).eq(SysApp::getName, "fkpfkpfkp"));
        System.out.println(sysApps);
    }

    @Test
    void testTableFieldExistFalse(){
        List<SysApp> sysApps = sysAppMapper.selectList(Wrappers.emptyWrapper());
        System.out.println(sysApps);
    }

    private String getInvalidAttrName(BadSqlGrammarException e){
        // 正常情况不会到这里，除非AttributeEnum的field字段和数据库表的字段不对应
        String notFoundAttrName = "attribute";
        if(e.getCause() instanceof SQLSyntaxErrorException){
            //Unknown column 'abc' in 'field list'
            String message = e.getCause().getMessage();
            notFoundAttrName = StringUtils.substringBetween(message, "Unknown column", "in 'field list'") + notFoundAttrName;
        }
        return notFoundAttrName;
    }

    @Test
    void testOpenGaussJdbc6AutoBalance(){
//        sysAppMapper.delete(Wrappers.emptyWrapper());
        List<SysApp> sysApps = sysAppMapper.selectList(Wrappers.emptyWrapper());
        List<SysApp> sysApps2 = sysAppMapper.selectList(Wrappers.emptyWrapper());
//        sysAppMapper.insert(new SysApp("001", "fkp", 25, "jinan", "abc", new Date()));
//        sysAppMapper.insert(new SysApp("002", "fkp2", 25, "jinan", "abc", new Date()));
//        sysAppMapper.insert(new SysApp("003", "fkp3", 25, "jinan", "abc", new Date()));
        List<SysApp> sysApps3 = sysAppMapper.selectList(Wrappers.emptyWrapper());
        List<SysApp> sysApps4 = sysAppMapper.selectList(Wrappers.emptyWrapper());

    }
}
