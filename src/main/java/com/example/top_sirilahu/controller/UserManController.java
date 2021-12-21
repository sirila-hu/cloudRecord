package com.example.top_sirilahu.controller;

import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.requestModel.PassChangeModel;
import com.example.top_sirilahu.service.userManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/userMan")
public class UserManController
{
    userManService userManService;

    public UserManController() {
    }

    @Autowired
    public UserManController(com.example.top_sirilahu.service.userManService userManService) {
        this.userManService = userManService;
    }

    @ResponseBody
    @RequestMapping(value = "/changePassword",produces = "application/json;charset=UTF-8")
    public String changePassword(@AuthenticationPrincipal userEntity user, PassChangeModel passChangeModel)
    {
        String message = "";
        try {
            userManService.changePass(user, passChangeModel);
            message = "{\"status\":0}";
        }catch (Exception e)
        {
            message = "{\"status\":1, \"message\":\"发生未知错误\"}";
            if (!StringUtils.isEmptyOrWhitespace(e.getMessage()))
            {
                message = String.format("{\"status\":1,\"message\":\"%s\"}", e.getMessage());
            }
            else
            {
                e.printStackTrace();
            }
        }
        finally
        {
            return message;
        }
    }
}
