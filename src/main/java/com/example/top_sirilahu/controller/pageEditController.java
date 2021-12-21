package com.example.top_sirilahu.controller;

import com.example.top_sirilahu.Exception.FileFormatNotCorrect;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.unitEntity;
import com.example.top_sirilahu.service.unitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/recordProject/pageEdit")
public class pageEditController
{
    private unitService uniService;

    public pageEditController() {
    }

    @Autowired
    public pageEditController(unitService uniService) {
        this.uniService = uniService;
    }

    @RequestMapping
    public String processPageEdit(pageEntity page, Model model)
    {
        //等待错误模板页面制作完成进行验证
        uniService.getUnits(page);
        model.addAttribute("page", page);
        return "pageEdit";
    }

    //添加单元
    @ResponseBody
    @RequestMapping(value = "/addUnit",produces = "application/json;charset=UTF-8")
    public String addUnit(@Valid unitEntity unit, @RequestParam(value = "u_imgFile", required = false)MultipartFile img, Errors errors)
    {
        if (errors.hasErrors())
        {
            return String.format("{\"status\":\"1\",\"message\":\"%s\"}", errors.getAllErrors().get(0).getDefaultMessage());
        }

        String resultJson = "";
        Map<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            //图片上传
            if (!img.isEmpty())
            {
                String u_img = uniService.imgUpload(img);
                unit.setU_img(u_img);
            }

            //添加单元
            String u_id = uniService.addUnit(unit);
            map.put("u_img", unit.getU_img());
            map.put("u_id", u_id);
            map.put("status","0");
            resultJson = mapper.writeValueAsString(map);
        }catch (IOException io)
        {
            io.printStackTrace();
            resultJson = "{\"status\":\"1\",\"message\":\"图片上传失败\"}";
        }
        catch (FileFormatNotCorrect e)
        {
            e.printStackTrace();
            resultJson = String.format("{\"status\":\"1\",\"message\":\"%s\"}", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (!StringUtils.isEmptyOrWhitespace(unit.getU_img()))
            {
                new File(unit.getU_img()).delete();
            }
            resultJson = "{\"status\":\"1\",\"message\":\"发生未知错误\"}";
        }
        finally {
            return resultJson;
        }
    }

    //修改单元文本内容
    @ResponseBody
    @RequestMapping(value = "/editUnit",produces = "application/json;charset=UTF-8")
    public String editUnit(unitEntity unit)
    {
        String resultJson = "";
        try {
            uniService.editUnit(unit);
            resultJson = "{\"status\":\"0\"}";
        }catch (Exception e)
        {
            e.printStackTrace();
            String message = "发生未知错误";
            if (!StringUtils.isEmptyOrWhitespace(message))
            {
                message = e.getMessage();
            }
            resultJson = String.format("{\"status\":\"1\",\"message\":\"%s\"}", message);
        }
        finally
        {
            return resultJson;
        }
    }

    //图片修改
    @ResponseBody
    @RequestMapping(value = "/changeImg",produces = "application/json;charset=UTF-8")
    public String changeImg(@RequestParam(value = "u_imgFile", required = false)MultipartFile img, @RequestParam(value = "u_id")String u_id)
    {
        String resultJson = "";
        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String newImg = uniService.editUnit(img, u_id);
            map.put("u_img", newImg);
            map.put("status","0");
            resultJson = mapper.writeValueAsString(map);
        }
        catch (NoSuchElementException e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\",\"message\":\"所选单元有误\"}";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String message = "发生未知错误";
            if (!StringUtils.isEmptyOrWhitespace(e.getMessage()))
            {
                message = e.getMessage();
            }
            resultJson = String.format("{\"status\":\"1\",\"message\":\"%s\"}", message);
        }
        finally
        {
            return resultJson;
        }
    }

    //单元删除
    @ResponseBody
    @RequestMapping(value = "/delUnit",produces = "application/json;charset=UTF-8")
    public String delUnit(@RequestParam("u_id") String u_id)
    {
        String resultJson = "";
        try {
            uniService.delUnit(u_id);
            resultJson = "{\"status\":\"0\"}";
        }
        catch (NoSuchElementException e)
        {
            e.printStackTrace();
            resultJson = "{\"status\":\"1\",\"message\":\"所选单元有误\"}";
        }catch (Exception e)
        {
            e.printStackTrace();
            String message = "发生未知错误";
            if (!StringUtils.isEmptyOrWhitespace(e.getMessage()))
            {
                message = e.getMessage();
            }
            resultJson = String.format("{\"status\":\"1\",\"message\":\"%s\"}", message);
        }
        finally
        {
            return resultJson;
        }
    }
}
