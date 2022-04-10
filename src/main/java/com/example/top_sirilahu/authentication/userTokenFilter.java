package com.example.top_sirilahu.authentication;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class userTokenFilter extends OncePerRequestFilter {
    private static final AntPathRequestMatcher IGNORE_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/recordProject/static/*/img/**", "GET");
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //[不稳定设置]关闭对图片存放区的认证，可能导致安全原因，需要优化
        if (IGNORE_ANT_PATH_REQUEST_MATCHER.matches(httpServletRequest))
        {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        try {
            //从Header中获取token
            String token = httpServletRequest.getHeader("authentication");
            if (StringUtils.isEmptyOrWhitespace(token))
            {
                this.failAuthentication(httpServletResponse, "未登录，请登陆后访问");
                return;
            }

            //认证token,并获取解析的认证信息
            Claims claims = JWTUtil.parseJWT(token);

            //解析claims获取用户对象
            userEntity user = JSON.parseObject(claims.get("user", String.class), userEntity.class);

            //根据用户对象船舰认证对象
            UserAuthentication userAuthentication = new UserAuthentication(user.getAuthorities(), user, "");
            userAuthentication.setDetails(new WebAuthenticationDetails(httpServletRequest));//设置详情
            //向上下文设置认证对象
            SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            //认证通过
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }catch (Exception e)
        {
            e.printStackTrace();
            this.failAuthentication(httpServletResponse, e.getMessage());
        }
    }

    //认证失败响应
    private void failAuthentication(HttpServletResponse response, String message)
    {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(403);
            String resultJSON = JSON.toJSONString(new statusJSON(1, message));
            response.getWriter().println(resultJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
