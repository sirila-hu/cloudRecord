package com.example.top_sirilahu.authentication;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler
{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSONString(new statusJSON(1, exception.getMessage())));
    }
}
