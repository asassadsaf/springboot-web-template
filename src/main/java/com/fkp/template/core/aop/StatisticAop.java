package com.fkp.template.core.aop;

import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.modules.authentication.entity.SysUserDetails;
import com.fkp.template.modules.statistic.service.StatisticService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/25 20:48
 */
@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class StatisticAop {
    @Autowired
    private StatisticService statisticService;

    @Pointcut("execution(* com.fkp.template.modules.*.controller..*.*(..)))")
    public void statisticPoint() {}

    @Around("statisticPoint()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = CommonConstant.DEFAULT_USERNAME;
        if(principal instanceof SysUserDetails){
            username = ((SysUserDetails) principal).getUsername();
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            statisticService.recordAkCallCount2Cache(username, false);
            throw e;
        }
        if(result instanceof RestSimpleResponse<?>){
            statisticService.recordAkCallCount2Cache(username, ((RestSimpleResponse<?>) result).isSuccessStatus());
        }
        //在Spring AOP的Around通知的proceed方法后半部分是所有通知类型最后执行的部分，此时response的isCommitted为false，可以设置响应头
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getResponse();
        response.addHeader("testHeaderAop", "test");
        return result;
    }

}
