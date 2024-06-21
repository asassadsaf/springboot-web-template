package com.fkp.template;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fkp.template.entity.DatabaseIntegrity;
import com.fkp.template.mapper.DatabaseIntegrityMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
}
