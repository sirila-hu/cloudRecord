package com.example.top_sirilahu.VO;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.top_sirilahu.entity.pageEntity;

public class pageVO {

    @JSONField(name = "p_id")
    private String p_id;


    @JSONField(name = "p_title")
    private String p_title;

    @JSONField(name = "p_date")
    private String p_date;

    //预览内容
    @JSONField(name = "p_pre")
    private String p_pre = "";

    //也文件路径
    @JSONField(name = "p_path")
    private String p_path = "";

    @JSONField(name = "p_section")
    private String p_section;

    @JSONField(name = "isActive")
    private boolean isActive = false;

    public pageVO() {
    }

    public pageVO(pageEntity page) {
        this.p_id = page.getP_id();
        this.p_title = page.getP_title();
        this.p_date = page.getP_date();
        this.p_pre = page.getP_pre();
        this.p_path = page.getP_path();
        this.p_section = page.getP_section();
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public String getP_date() {
        return p_date;
    }

    public void setP_date(String p_date) {
        this.p_date = p_date;
    }

    public String getP_pre() {
        return p_pre;
    }

    public void setP_pre(String p_pre) {
        this.p_pre = p_pre;
    }

    public String getP_path() {
        return p_path;
    }

    public void setP_path(String p_path) {
        this.p_path = p_path;
    }

    public String getP_section() {
        return p_section;
    }

    public void setP_section(String p_section) {
        this.p_section = p_section;
    }
}
