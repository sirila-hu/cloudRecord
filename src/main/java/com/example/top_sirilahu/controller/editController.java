package com.example.top_sirilahu.controller;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.service.editService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/recordProject/recordEdit", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*")
public class editController {
    private editService edit_Service;

    public editController() {
    }

    @Autowired
    public editController(editService edit_Service) {
        this.edit_Service = edit_Service;
    }

    //渲染编辑页面
    @GetMapping("/{r_id}")
    public String processRecordEdit(@PathVariable("r_id") String r_id) {
        //填充渲染项目
        return edit_Service.getProcessItem(r_id);
    }

    //添加分区
    @PostMapping("/{r_id}")
    public ResponseEntity addSection(@PathVariable("r_id") String r_id, @Valid sectionEntity section, Errors errors) {
        //检测并返回映射结果
        if (errors.hasErrors()) {
            String responseJSON = JSON.toJSONString(new statusJSON(1, errors.getAllErrors().get(0).getDefaultMessage()));
            return new ResponseEntity<>(responseJSON, HttpStatus.BAD_REQUEST);
        }

        try {
            section.setS_record(r_id);
            //执行分区添加
            String responseJSON = edit_Service.addSection(section);
            //返回响应json
            return new ResponseEntity<>(responseJSON, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(JSON.toJSONString(new statusJSON(1, e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改分区
     *
     * @param s_id    待修改分区ID
     * @param s_title 修改分区值
     * @return ResponseEntity
     */
    @PutMapping("/section/{s_id}")
    public ResponseEntity editSection(@PathVariable("s_id") String s_id, @RequestParam(value = "s_title", required = true) String s_title) {
        //合法性检查
        if (StringUtils.isEmptyOrWhitespace(s_title)) {
            String responseJSON = JSON.toJSONString(new statusJSON(1, "必要参数不能为空"));
            return new ResponseEntity<>(responseJSON, HttpStatus.BAD_REQUEST);
        } else if (s_title.length() > 45) {
            String responseJSON = JSON.toJSONString(new statusJSON(1, "分区名大于45长度，请修改"));
            return new ResponseEntity<>(responseJSON, HttpStatus.BAD_REQUEST);
        }

        String resultJson = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            //调用修改业务逻辑
            edit_Service.editSection(s_title, s_id);
            resultJson = JSON.toJSONString(new statusJSON(0, "修改成功"));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, "该分区不存在"));
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, "发生未知错误"));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity<>(resultJson, httpStatus);
        }

    }

    /**
     * 删除分区
     *
     * @param s_id 待删除分区的ID
     * @return
     */
    @DeleteMapping("/section/{s_id}")
    public ResponseEntity delSection(@PathVariable("s_id") String s_id) {
        String resultJson = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            edit_Service.delSection(s_id);
            resultJson = JSON.toJSONString(new statusJSON(0, "删除成功"));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, "未找到该分区"));
            ;
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            ;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity<>(resultJson, httpStatus);
        }
    }

    /**
     * 添加页
     *
     * @param s_id   添加页的分区ID
     * @param page   添加页的信息
     * @param errors
     * @return
     */
    @PostMapping("/section/{s_id}")
    public ResponseEntity addPage(@PathVariable("s_id") String s_id, @Valid pageEntity page, Errors errors) {
        if (errors.hasErrors()) {
            String responseJSON = JSON.toJSONString(new statusJSON(1, errors.getAllErrors().get(0).getDefaultMessage()));
            return new ResponseEntity<>(responseJSON, HttpStatus.BAD_REQUEST);
        }

        String resultJson = "";
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            page.setP_section(s_id);
            resultJson = edit_Service.addPage(page);
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, e.getMessage()));
        } finally {
            return new ResponseEntity<>(resultJson, httpStatus);
        }


    }

    //修改页
    @PutMapping("/page/{p_id}")
    public ResponseEntity editPage(@PathVariable("p_id") String p_id, @RequestParam(value = "p_title", required = true) String p_title) {
        //合法性校验
        if (StringUtils.isEmptyOrWhitespace(p_title)) {
            String responseJSON = JSON.toJSONString(new statusJSON(1, "必要参数不能为空"));
            return new ResponseEntity<>(responseJSON, HttpStatus.BAD_REQUEST);
        }

        String resultJson = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            //修改逻辑调用
            edit_Service.editPage(p_title, p_id);
            resultJson = JSON.toJSONString(new statusJSON(0, "修改成功"));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, "修改页不存在"));
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity<>(resultJson, httpStatus);
        }
    }

    //删除页
    @DeleteMapping("/page/{p_id}")
    public ResponseEntity delPage(@PathVariable("p_id") String p_id) {
        String resultJson = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            edit_Service.delPage(p_id);
            resultJson = JSON.toJSONString(new statusJSON(0, "删除成功"));
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity<>(resultJson, httpStatus);
        }
    }
}
