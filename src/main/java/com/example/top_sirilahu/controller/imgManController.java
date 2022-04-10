package com.example.top_sirilahu.controller;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.service.imgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;

@RestController
@RequestMapping(value = "/recordProject/imgMan", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*")
public class imgManController {
    private imgService imgSe;

    @Autowired
    public imgManController(imgService imgSe) {
        this.imgSe = imgSe;
    }

    /**
     * 获取图片上传记录
     *
     * @param page 获取的页码
     * @return
     */
    @GetMapping
    public ResponseEntity getImgInfo(@RequestParam(value = "page", defaultValue = "1") int page, @AuthenticationPrincipal userEntity user) {
        String resultJSON = "";
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            resultJSON = imgSe.getImgInfoPagination(page, user.getUID());
        } catch (NoResultException ne) {
            resultJSON = JSON.toJSONString(new statusJSON(0, ne.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            resultJSON = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity<>(resultJSON, httpStatus);
        }
    }

    /**
     * 图片上传节点(增加)
     *
     * @param img 待上传的图片
     * @return
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity uploadImg(@RequestParam(value = "img") MultipartFile img, @AuthenticationPrincipal userEntity user) {
        //合法性校验
        if (img.getContentType().indexOf("image") == -1) {
            String responJSON = JSON.toJSONString(new statusJSON(1, "文件格式不正确，只接受图像类型数据"));
            return new ResponseEntity<>(responJSON, HttpStatus.BAD_REQUEST);
        }

        String resultJSON = "";
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            String path = imgSe.imgUpload(img, user.getUID());
            resultJSON = JSON.toJSONString(new statusJSON(0, "上传成功", path));
        } catch (Exception e) {
            e.printStackTrace();
            resultJSON = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity(resultJSON, httpStatus);
        }
    }

    @DeleteMapping("/{imgID}")
    public ResponseEntity delImg(@PathVariable("imgID") long imgID, @RequestParam(value = "page", defaultValue = "1") int page, @AuthenticationPrincipal userEntity user) {
        String resultJSON = "";
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            //调用图片删除服务
            imgSe.delImg(imgID);

            //调用图片记录获取服务重新渲染页面
            resultJSON = imgSe.getImgInfoPagination(page, user.getUID());
        } catch (Exception e) {
            e.printStackTrace();
            resultJSON = JSON.toJSONString(new statusJSON(1, e.getMessage()));
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            return new ResponseEntity<>(resultJSON, httpStatus);
        }
    }


}
