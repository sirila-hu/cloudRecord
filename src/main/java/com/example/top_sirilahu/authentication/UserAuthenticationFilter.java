package com.example.top_sirilahu.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    SessionRegistry sessionRegistry;

    private String usernameParameter = "username";
    private String passwordParameter = "password";

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public UserAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authentication/login"));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "usernameParameter 不允许为空值");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "passwordParameter 不允许为空值");
        this.passwordParameter = passwordParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        //获取前端提交的登陆信息
        String username = this.getUsername(httpServletRequest);
        String password = this.getPassword(httpServletRequest);

        //检查登陆信息完整性
        if(StringUtils.isEmptyOrWhitespace(username) || StringUtils.isEmptyOrWhitespace(password))
        {
            throw new UsernameNotFoundException("请完整填写登陆信息");
        }

        //创建未认证的认证对象
        UserAuthentication userAuthentication = new UserAuthentication(username, password);

        //设置详情
        this.setDetails(httpServletRequest, userAuthentication);

        //进行认证并获取认证完成的认证对象
        Authentication verifiedAuthentication = this.getAuthenticationManager().authenticate(userAuthentication);

        //注册session
        sessionRegistry.registerNewSession(httpServletRequest.getSession().getId(), verifiedAuthentication.getPrincipal());

        return verifiedAuthentication;
    }

    //设置详情
    public void setDetails(HttpServletRequest request, UserAuthentication userAuthentication)
    {
        userAuthentication.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    //从请求对象中获取username参数
    public String getUsername(HttpServletRequest request)
    {
        return request.getParameter(usernameParameter);
    }

    //从请求对象中获取password参数
    public String getPassword(HttpServletRequest request)
    {
        return request.getParameter(passwordParameter);
    }
}
