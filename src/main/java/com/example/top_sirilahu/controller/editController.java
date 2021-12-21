package com.example.top_sirilahu.controller;

import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.requestModel.editPageRequest;
import com.example.top_sirilahu.requestModel.editSectionRequest;
import com.example.top_sirilahu.requestModel.processModel;
import com.example.top_sirilahu.service.editService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.AbstractSequentialList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Controller
@RequestMapping("/recordProject/recordEdit")
public class editController
{
    private editService edit_Service;

    public editController() {
    }

    @Autowired
    public editController(editService edit_Service) {
        this.edit_Service = edit_Service;
    }

    //渲染编辑页面
    @RequestMapping
    public String processRecordEdit(@Valid recordEntity record, Model model, processModel preModel)
    {
        //填充渲染项目
        edit_Service.getProcessItem(record);
        //传递渲染项目
        model.addAttribute("record", record);
        model.addAttribute("preModel", preModel);
        return "recordEdit";
    }

    //添加分区
    @ResponseBody
    @RequestMapping(value = "/addSection",produces = "application/json;charset=UTF-8")
    public String addSection(@Valid sectionEntity section, Errors errors)
    {
        //检测并返回映射结果
        if(errors.hasErrors())
        {
            return String.format("{\"status\":\"1\",\"message\":\"%s\"}", errors.getAllErrors().get(0).getDefaultMessage());
        }
        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = "";
        try {
            //执行分区添加
            String s_id = edit_Service.addSection(section);
            map.put("s_id", s_id);
            map.put("status","0");
            resultJson = mapper.writeValueAsString(map);
        }catch (Exception e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\"}";
        }finally {
            return resultJson;
        }
    }

    //修改分区
    @ResponseBody
    @RequestMapping(value = "/editSection",produces = "application/json;charset=UTF-8")
    public String editSection(@Valid editSectionRequest sectionRequest, Errors errors)
    {
        if (errors.hasErrors())
        {
            return String.format("{\"status\":\"1\",\"message\":\"%s\"}", errors.getAllErrors().get(0).getDefaultMessage());
        }

        String resultJson = "";
        try {
            edit_Service.editSection(sectionRequest.getS_title(), sectionRequest.getS_id());
            resultJson = "{\"status\":\"0\"}";
        }catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\",\"message\":\"修改分区不存在\"}";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\"}";
        }finally {
            return resultJson;
        }

    }

    //删除分区
    @ResponseBody
    @RequestMapping(value = "/delSection",produces = "application/json;charset=UTF-8")
    public String delSection(@RequestParam(value = "s_id") String s_id)
    {
        String resultJson = "";
        try {
            edit_Service.delSection(s_id);
            resultJson = "{\"status\":\"0\"}";
        }catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\",\"message\":\"该分区不存在\"}";
        }catch (Exception e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\"}";
        }finally {
            return resultJson;
        }
    }

    //添加页
    @ResponseBody
    @RequestMapping(value = "/addPage",produces = "application/json;charset=UTF-8")
    public String addPage(@Valid pageEntity page, Errors errors)
    {
        if (errors.hasErrors())
        {
            return String.format("{\"status\":\"1\",\"message\":\"%s\"}", errors.getAllErrors().get(0).getDefaultMessage());
        }

        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = "";
        try {
            edit_Service.addPage(page);
            map.put("status", "0");
            map.put("p_id", page.getP_id());
            resultJson = mapper.writeValueAsString(map);
        }catch (Exception e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\"}";
        }finally {
            return resultJson;
        }


    }

    //修改页
    @ResponseBody
    @RequestMapping(value = "/editPage",produces = "application/json;charset=UTF-8")
    public String editPage(@Valid editPageRequest pageRequest, Errors errors)
    {
        if (errors.hasErrors())
        {
            return String.format("{\"status\":\"1\",\"message\":\"%s\"}", errors.getAllErrors().get(0).getDefaultMessage());
        }

        String resultJson = "";
        try {
            edit_Service.editPage(pageRequest.getP_title(), pageRequest.getP_id());
            resultJson = "{\"status\":\"0\"}";
        }catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\",\"message\":\"修改页不存在\"}";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\"}";
        }finally {
            return resultJson;
        }
    }

    //删除页
    @ResponseBody
    @RequestMapping(value = "/delPage",produces = "application/json;charset=UTF-8")
    public String delPage(@RequestParam(value = "p_id") String p_id)
    {
        String resultJson = "";
        try {
            edit_Service.delPage(p_id);
            resultJson = "{\"status\":\"0\"}";
        }catch (EmptyResultDataAccessException e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\",\"message\":\"该分区不存在\"}";
        }catch (Exception e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\"}";
        }finally {
            return resultJson;
        }
    }
}
