package com.example.top_sirilahu.controller;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.VO.passwordVO;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.service.userManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@RestController
@RequestMapping(value = "/recordProject/user", produces = "application/json;charset=UTF-8")
@CrossOrigin("*")
public class UserManController {
    userManService userManService;

    public UserManController() {
    }

    @Autowired
    public UserManController(com.example.top_sirilahu.service.userManService userManService) {
        this.userManService = userManService;
    }

    @PutMapping(value = "/password")
    public ResponseEntity changePassword(@AuthenticationPrincipal userEntity user, passwordVO password)
    {
        String resultJSON = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            userManService.changePass(user, password);
            resultJSON = JSON.toJSONString(new statusJSON(0, "密码更改成功"));
        }catch (Exception e)
        {
            e.printStackTrace();

            String message = "发生未知错误";
            if (!StringUtils.isEmptyOrWhitespace(e.getMessage()))
            {
                message = e.getMessage();
            }
            resultJSON = JSON.toJSONString(new statusJSON(1, message));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        finally
        {
            return new ResponseEntity<>(resultJSON,httpStatus);
        }
    }
}
