package com.example.top_sirilahu.authentication;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.service.AuthenticatService;
import com.example.top_sirilahu.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Data 2022/4/10
 * @Doc jwt认证拦截器
 */
@Component("userTokenFilter")
public class userTokenFilter extends OncePerRequestFilter {
    @Autowired
    AuthenticatService authService;

    private static final AntPathRequestMatcher IGNORE_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/recordProject/static/*/img/**", "GET");

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //[不稳定设置]关闭对图片存放区的认证，可能导致安全原因，需要优化
        if (IGNORE_ANT_PATH_REQUEST_MATCHER.matches(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        try {
            //从Header中获取token
            String token = httpServletRequest.getHeader("authentication");
            Assert.hasText(token, "未登录，请登陆后访问");

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
        } catch (ExpiredJwtException e) {
            //令牌过期，尝试能否刷新
            this.refreshToken(httpServletRequest, httpServletResponse, filterChain);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            this.failAuthentication(httpServletResponse, e.getMessage());
        }
    }

    /**
     * - 令牌刷新逻辑 -
     * @param request 请求对象
     * @param response 响应对象
     * @param filterChain 过滤链对象
     */
    private void refreshToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        try {
            //获取refresh_token
            String refresh_token = request.getHeader("refresh");
            Assert.hasText(refresh_token, "凭证已过期，请重新登陆");

            //认证token,并获取解析的认证信息
            Claims claims = JWTUtil.parseJWT(refresh_token);

            Long UID = Long.valueOf(claims.get("UID").toString()) ;
            //重新获取用户对象
            userEntity user = (userEntity) authService.loadUserByUID(UID);

            //创建新token
            String token = JWTUtil.createJWT(user);
            refresh_token = JWTUtil.createRefresh(user.getUID());

            //将新token放到respond的header中
            response.setHeader("access_token", token);
            response.setHeader("refresh", refresh_token);

            //向上下文设置认证对象
            SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user.getAuthorities(), user, ""));

            //认证通过
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            this.failAuthentication(response, "凭证刷新失败，请重新登陆");
        }
    }

    /**
     * - 认证失败响应 -
     * @param response 响应对象
     * @param message 响应信息
     */
    private void failAuthentication(HttpServletResponse response, String message) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            String resultJSON = JSON.toJSONString(new statusJSON(1, message));
            response.getWriter().println(resultJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
