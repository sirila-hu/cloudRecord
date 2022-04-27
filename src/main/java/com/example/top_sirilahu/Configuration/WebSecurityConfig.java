package com.example.top_sirilahu.Configuration;

import com.example.top_sirilahu.authentication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Autowired
    AuthenticationFailHandler failHandler;

    @Autowired
    UserAuthenticationProvider userAuthProvider;

    //创建jwt认证过滤器
    @Autowired
    userTokenFilter userTokenFilter;

    //安全规则配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //创建自定义登陆过滤器
        UserAuthenticationFilter userAuthFilter = new UserAuthenticationFilter();

        //配置过滤器
        userAuthFilter.setAuthenticationManager(new UserAuthenticationManager(userAuthProvider));
        userAuthFilter.setAuthenticationSuccessHandler(successHandler);
        userAuthFilter.setAuthenticationFailureHandler(failHandler);

        http.headers().frameOptions().disable();
        //禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 登陆验证信息
        http.authenticationProvider(userAuthProvider)
                .authorizeRequests()
                .antMatchers("/recordProject/static/*/img/**")//不安全配置
                    .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();

        //认证设置
        http.csrf().disable()
                .addFilterAt(userAuthFilter, UsernamePasswordAuthenticationFilter.class)//设置登陆验证过滤器
                .addFilterAfter(userTokenFilter, UserAuthenticationFilter.class);
    }

}
