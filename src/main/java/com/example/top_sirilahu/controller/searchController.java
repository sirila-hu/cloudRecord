package com.example.top_sirilahu.controller;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.searchJSON;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.service.searchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping(value = "/recordProject/search", produces = "application/json;charset=UTF-8")
@CrossOrigin("*")
public class searchController {
    searchService seaService;

    public searchController() {
    }

    @Autowired
    public searchController(searchService seaService) {
        this.seaService = seaService;
    }

    @GetMapping()
    public ResponseEntity globalSearch(@RequestParam(value = "field") String field, @AuthenticationPrincipal userEntity user)
    {
        String resultJSON = "";
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            List sectionResult = seaService.sectionSearch(user.getUID(), field);
            List pageResult = seaService.pageSearch(user.getUID(), field);
            resultJSON = JSON.toJSONString(new searchJSON(0, "查询成功", sectionResult, pageResult));
        }catch (Exception e)
        {
            e.printStackTrace();
            resultJSON = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }finally {
            return new ResponseEntity(resultJSON, httpStatus);
        }
    }
}
