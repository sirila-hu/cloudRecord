package com.example.top_sirilahu.controller;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.service.pageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@RestController
@RequestMapping(value = "/recordProject/pageEdit", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*")
public class pageEditController {
    private pageFileService pageEditService;

    public pageEditController() {
    }

    @Autowired
    public pageEditController(pageFileService pageEditService) {
        this.pageEditService = pageEditService;
    }

    /**
     * 保存页文件的控制器方法
     *
     * @param p_id        页ID 用于更新页信息和页文件命名
     * @param pageContent 待保存的页内容
     * @return
     */
    @PostMapping(value = "/{p_id}")
    public ResponseEntity savePage(@PathVariable("p_id") String p_id, @RequestParam(value = "pageContent") String pageContent, @AuthenticationPrincipal userEntity user) {
        //合法性验证
        if (StringUtils.isEmptyOrWhitespace(pageContent)) {
            String responseJSON = JSON.toJSONString(new statusJSON(1, "保存内容不能为空"));
            return new ResponseEntity(responseJSON, HttpStatus.BAD_REQUEST);
        }

        String resultJson = "";
        HttpStatus httpStatus = HttpStatus.CREATED;

        //调用服务层保存页文件
        try {
            String p_path = pageEditService.savePage(pageContent, p_id, user.getUID());
            resultJson = JSON.toJSONString(new statusJSON(0, "保存成功", p_path));
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity(resultJson, httpStatus);
        }
    }
}
