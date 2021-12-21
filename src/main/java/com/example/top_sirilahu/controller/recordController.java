package com.example.top_sirilahu.controller;

import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.service.recordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recordProject/recordMan")

public class recordController
{
    private recordService reService;

    public recordController() {}

    @Autowired
    public recordController(recordService reService) {
        this.reService = reService;
    }

    @GetMapping
    public String processRecordMan(@RequestParam(name = "page", defaultValue = "1") int page, Model model, @AuthenticationPrincipal userEntity user)
    {
        //执行渲染
        reService.getRecords(user, page, model);
        return "recordMan";
    }

    @RequestMapping("/addRecord")
    public String addRecord(@RequestParam(name = "page", defaultValue = "1") int page, Model model, recordEntity record,  @AuthenticationPrincipal userEntity user)
    {
        //添加记录
        reService.addRecord(record, user);

        return String.format("redirect:/recordProject/recordMan?page=%d", page);
    }

    @RequestMapping("/editRecord")
    public String editRecord(@RequestParam(name = "page", defaultValue = "1") int page, recordEntity record)
    {
        try {
            //修改记录
            reService.editRecord(record.getR_name(), record.getR_id());
        }catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
        }finally {
            return String.format("redirect:/recordProject/recordMan?page=%d", page);
        }
    }

    @RequestMapping("/delRecord")
    public String delRecord(@RequestParam(name = "page", defaultValue = "1") int page, recordEntity record)
    {
        try {
            //删除记录
            reService.delRecord(record.getR_id());
        }catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
        }finally {
            return String.format("redirect:/recordProject/recordMan?page=%d", page);
        }
    }

}
