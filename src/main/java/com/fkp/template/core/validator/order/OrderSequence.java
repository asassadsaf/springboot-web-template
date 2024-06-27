package com.fkp.template.core.validator.order;

import javax.validation.GroupSequence;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description Validator框架分组序列，使用Validator注解时确认其校验顺序
 * @date 2024/6/18 11:44
 */

@GroupSequence({OrderSequence.OrderA.class, OrderSequence.OrderB.class, OrderSequence.OrderC.class, OrderSequence.OrderD.class,
        OrderSequence.OrderE.class, OrderSequence.OrderF.class, OrderSequence.OrderG.class, OrderSequence.OrderH.class,
        OrderSequence.OrderI.class, OrderSequence.OrderJ.class, OrderSequence.OrderK.class, OrderSequence.OrderL.class, OrderSequence.OrderM.class})
public interface OrderSequence {
    interface OrderA{}
    interface OrderB{}
    interface OrderC{}
    interface OrderD{}
    interface OrderE{}
    interface OrderF{}
    interface OrderG{}
    interface OrderH{}
    interface OrderI{}
    interface OrderJ{}
    interface OrderK{}
    interface OrderL{}
    interface OrderM{}
}
