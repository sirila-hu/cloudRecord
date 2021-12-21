package com.example.top_sirilahu.authentication;

import com.example.top_sirilahu.service.AuthenticatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;

@Component("UserAuthenticationProvider")
public class UserAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    AuthenticatService authService;

    @Autowired
    UserPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication userAuthentication = (UserAuthentication) authentication;
        UserAuthentication verifiedAuthentication = null;
        try {
            //获取数据库中用户信息
            UserDetails user = authService.loadUserByUsername(userAuthentication.getPrincipal().toString());

            //进行认证
            if (!passwordEncoder.matches((CharSequence) userAuthentication.getCredentials(), user.getPassword()))
            {
                throw new Exception("密码不正确");
            }

            //认证成功创建已认证的认证对象
            verifiedAuthentication = new UserAuthentication(user.getAuthorities(), user, user.getPassword());

            //设置详情
            verifiedAuthentication.setDetails(userAuthentication.getDetails());

        }catch (Exception e)
        {
            String message = "发生未知错误，认证失败";
            if (StringUtils.isEmptyOrWhitespace(e.getMessage()))
            {
                e.printStackTrace();
            }
            else
            {
                message = e.getMessage();
            }

            throw new BadCredentialsException(message);
        }

        return verifiedAuthentication;

    }

    /**
     *指定该认证处理器认证的对象
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return UserAuthentication.class.isAssignableFrom(aClass);
    }
}
