package com.example.top_sirilahu.Configuration;

import Listener.SirilahuHttpSessionEventPublisher;
import com.example.top_sirilahu.authentication.*;
import com.example.top_sirilahu.service.AuthenticatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Autowired
    AuthenticationFailHandler failHandler;

    @Autowired
    UserAuthenticationProvider userAuthProvider;

    @Autowired
    AuthenticatService authenticatService;

    @Resource
    SessionRegistry sessionRegistry;


    //安全规则配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //创建自定义过滤器
        UserAuthenticationFilter userAuthFilter = new UserAuthenticationFilter();

        //配置过滤器
        userAuthFilter.setAuthenticationManager(new UserAuthenticationManager(userAuthProvider));
        userAuthFilter.setSessionAuthenticationStrategy(new UserSessionControlAuthenticationStrategy(sessionRegistry));
        userAuthFilter.setSessionRegistry(sessionRegistry);
        userAuthFilter.setAuthenticationSuccessHandler(successHandler);
        userAuthFilter.setAuthenticationFailureHandler(failHandler);

        http.headers().frameOptions().disable();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/recordProject/*")
                    .authenticated()
                .antMatchers("/", "/**")
                    .permitAll()
                .and()
                    .logout().logoutSuccessUrl("/").permitAll()
                .and()
                    .formLogin()
                    .loginPage("/")
                    .loginProcessingUrl("/authentication/login").permitAll();

        //设置登陆验证过滤器
        http.addFilterAt(userAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //设置并发session过滤器
        http.addFilterAt(new ConcurrentSessionFilter(sessionRegistry, sessionInformationExpiredStrategy()), ConcurrentSessionFilter.class);
        //向session监听器设置登陆表
        SirilahuHttpSessionEventPublisher.setSessionRegistry(sessionRegistry);
    }


    @Bean
    public SessionRegistry sessionRegistry()
    {
        return new SessionRegistryImpl();
    }

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy()
    {
        return new SimpleRedirectSessionInformationExpiredStrategy("/");
    }
}
