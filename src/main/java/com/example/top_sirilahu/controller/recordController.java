package com.example.top_sirilahu.controller;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.Exception.RunException;
import com.example.top_sirilahu.VO.pageSortVO;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.service.recordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/recordProject/recordMan", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*")
public class recordController {
    private recordService reService;

    public recordController() {
    }

    @Autowired
    public recordController(recordService reService) {
        this.reService = reService;
    }

    @GetMapping
    public ResponseEntity processRecordMan(@Valid @ModelAttribute("pageSortVO") pageSortVO pageSort ,@AuthenticationPrincipal userEntity user, Errors errors) {
        String resultJOSN = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            resultJOSN = reService.getRecords(user, pageSort);
        }catch (RunException e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            resultJOSN = e.getResultJSON();
        }finally {
            //返回渲染数据
            return new ResponseEntity(resultJOSN, httpStatus);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addRecord(@Valid pageSortVO pageSort, recordEntity record, @AuthenticationPrincipal userEntity user) throws RunException {
        try {
            //添加记录
            reService.addRecord(record, user);
        } catch (Exception e) {
            return JSON.toJSONString(new statusJSON(1, "添加失败"));
        }

        //返回添加后的本页信息
        return reService.getRecords(user, pageSort);
    }

    @PutMapping("/{r_id}")
    public String editRecord(@PathVariable("r_id") String r_id, @RequestParam(name = "r_name", required = true) String r_name) {
        if (StringUtils.isEmptyOrWhitespace(r_name)) {
            String responseJSON = JSON.toJSONString(new statusJSON(1, "必要参数不能为空"));
            return responseJSON;
        }
        String json = JSON.toJSONString(new statusJSON(0, "修改成功"));
        try {
            //修改记录
            reService.editRecord(r_name, r_id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            json = JSON.toJSONString(new statusJSON(1, e.getMessage()));
        } finally {
            return json;
        }
    }

    @DeleteMapping("/{r_id}")
    public String delRecord(@PathVariable("r_id") String r_id, @Valid pageSortVO pageSort, @AuthenticationPrincipal userEntity user) throws RunException {
        try {
            //删除记录
            reService.delRecord(r_id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return JSON.toJSONString(new statusJSON(1, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回删除后的本页信息
        return reService.getRecords(user, pageSort);
    }

}
