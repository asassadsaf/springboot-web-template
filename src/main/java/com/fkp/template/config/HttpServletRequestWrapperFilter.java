package com.fkp.template.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description servlet过滤器，用于包装request对象
 * @date 2024/4/29 10:56
 */
@WebFilter(urlPatterns = "/*", filterName = "httpServletRequestWrapperFilter")
public class HttpServletRequestWrapperFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper((HttpServletRequest) request);

        chain.doFilter(myHttpServletRequestWrapper, response);

    }
}
