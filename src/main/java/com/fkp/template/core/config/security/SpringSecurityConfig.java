package com.fkp.template.core.config.security;

import com.fkp.template.core.constant.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/24 14:40
 */
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService sysUserDetailsServiceImpl, SynthesisPasswordEncoder synthesisPasswordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(sysUserDetailsServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(synthesisPasswordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, AuthenticationEntryPointImpl authenticationEntryPointImpl) throws Exception {
        return httpSecurity
                // CSRF禁用，因为不使用session
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用HTTP响应标头
                .headers((headersCustomizer) -> {
                    headersCustomizer.cacheControl(HeadersConfigurer.CacheControlConfig::disable).frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                })
                // 认证失败处理类
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPointImpl))
                // 基于token，所以不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 注解标记允许匿名访问的url
                .authorizeHttpRequests((requests) -> {
//                    permitAllUrl.getUrls().forEach(url -> requests.antMatchers(url).permitAll());
                    // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                    requests.antMatchers("/test/v1/authentication/login").permitAll()
                            // 静态资源，可匿名访问
//                            .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                            .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                            // 除上面外的所有请求全部需要鉴权认证
//                            .anyRequest().authenticated();
                            //测试使用，允许所有接口不鉴权
                            .anyRequest().permitAll();
                })
                // 添加Logout filter
//                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler))
                // 添加JWT filter
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加CORS filter
//                .addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class)
//                .addFilterBefore(corsFilter, LogoutFilter.class)
                .build();
    }
}
