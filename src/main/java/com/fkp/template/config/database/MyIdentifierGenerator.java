package com.fkp.template.config.database;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.fkp.template.util.StrIdGenerator;

import java.math.BigInteger;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description MybatisPlus自动填充id配置
 * @date 2023/7/7 14:12
 */

public class MyIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return new BigInteger(StrIdGenerator.getInstance().nextId());
    }

}
