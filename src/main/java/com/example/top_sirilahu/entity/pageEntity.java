package com.example.top_sirilahu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "tb_page")
public class pageEntity
{
    @Id
    @Column(name = "p_id")
    @JSONField(name = "p_id")
    private String p_id;

    @Column(name = "p_title")
    @NotBlank(message = "标题不能为空")
    @JSONField(name = "p_title")
    private String p_title;

    @Column(name = "p_date")
    @JSONField(name = "p_date")
    private String p_date;

    @Column(name = "p_pre")
    @JSONField(name = "p_pre")
    private String p_pre = "";

    @Column(name = "p_path")
    @JSONField(name = "p_path")
    private String p_path = "";

    @Column(name = "p_section")
    private String p_section;

    @ManyToOne
    @JoinColumn(name = "p_section", insertable = false, updatable = false)
    @JSONField(name = "section")
    private sectionEntity section;


    public pageEntity() {
    }

    public pageEntity(String p_id, String p_title, String p_date, String p_pre, String p_path, String p_section) {
        this.p_id = p_id;
        this.p_title = p_title;
        this.p_date = p_date;
        this.p_pre = p_pre;
        this.p_path = p_path;
        this.p_section = p_section;
    }

    public sectionEntity getSection() {
        return section;
    }

    public void setSection(sectionEntity section) {
        this.section = section;
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

    public String getP_section() {
        return p_section;
    }

    public void setP_section(String p_section) {
        this.p_section = p_section;
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
}
