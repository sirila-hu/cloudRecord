package com.example.top_sirilahu.VO;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.top_sirilahu.entity.imgEntity;
import com.example.top_sirilahu.entity.recordEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

public class imageVO {

    @JSONField(name = "imgID")
    private long imgID;

    @JSONField(name = "imgName")
    private String imgName;

    @JSONField(name = "path")
    private String path;

    @JSONField(name = "uploadDate")
    private String uploadDate;

    @JSONField(name = "uploader")
    private long uploader;

    public imageVO(imgEntity image) {
        this.imgID = image.getImgID();
        this.imgName = image.getImgName();
        this.path = image.getPath();
        this.uploadDate = image.getUploadDate();
        this.uploader = image.getUploader();
    }

    public static List convertToAll(List<imgEntity> entities)
    {
        List VOS = new ArrayList<>();
        //遍历转换
        for (imgEntity entity: entities)
        {
            VOS.add(new imageVO(entity));
        }

        return VOS;
    }

    public long getImgID() {
        return imgID;
    }

    public void setImgID(long imgID) {
        this.imgID = imgID;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public long getUploader() {
        return uploader;
    }

    public void setUploader(long uploader) {
        this.uploader = uploader;
    }
}
