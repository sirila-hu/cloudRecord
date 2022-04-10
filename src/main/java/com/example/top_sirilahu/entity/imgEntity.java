package com.example.top_sirilahu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_image")
public class imgEntity
{
    @Id
    @Column(name = "imgID")
    private Long imgID;

    @Column(name = "imgName")
    private String imgName;

    @Column(name = "path")
    private String path;

    @Column(name = "uploadDate")
    private String uploadDate;

    @Column(name = "uploader")
    private long uploader;

    public imgEntity() {
    }

    public imgEntity(String imgName, String path, long uploader) {
        this.imgName = imgName;
        this.path = path;
        this.uploader = uploader;
    }

    public imgEntity(long imgID, String imgName, String path, String uploadDate, long uploader) {
        this.imgID = imgID;
        this.imgName = imgName;
        this.path = path;
        this.uploadDate = uploadDate;
        this.uploader = uploader;
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
