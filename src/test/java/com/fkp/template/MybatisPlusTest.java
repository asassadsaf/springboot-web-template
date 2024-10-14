package com.fkp.template;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fkp.template.core.util.StrIdGenerator;
import com.fkp.template.modules.app.entity.SysApp;
import com.fkp.template.modules.app.mapper.SysAppMapper;
import com.fkp.template.modules.dbintegrity.entity.DatabaseIntegrity;
import com.fkp.template.modules.dbintegrity.mapper.DatabaseIntegrityMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
}
