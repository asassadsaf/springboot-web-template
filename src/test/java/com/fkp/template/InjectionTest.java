package com.fkp.template;

import com.fkp.template.modules.dbintegrity.entity.ChildTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/12 15:58
 */
@SpringBootTest
public class InjectionTest {

    @Autowired
    private ChildTest childTest;

    @Test
    void test(){
        System.out.println(childTest);
    }
}
