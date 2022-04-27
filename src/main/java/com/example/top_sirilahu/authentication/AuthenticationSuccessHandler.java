package com.example.top_sirilahu.authentication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        userEntity user = (userEntity) authentication.getPrincipal();
        Map json = new HashMap();
        //登陆成功生成access_token
        json.put("access_token", JWTUtil.createJWT(user));

        //生成refresh_token
        json.put("refresh_token", JWTUtil.createRefresh(user.getUID()));

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSONString(json));
    }
}
