package com.example.top_sirilahu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_section")
public class sectionEntity
{
    @Id
    @Column(name = "s_id")
    @JSONField(name = "s_id")
    private String s_id;

    @NotBlank(message = "标题不能为空")
    @Size(max = 45, message = "标题字数不能大于45")
    @Column(name = "s_title")
    @JSONField(name = "s_title")
    private String s_title;

    @Column(name = "s_date")
    @JSONField(name = "s_date")
    private String s_date;

    @Column(name = "s_record")
    private String s_record;

    @ManyToOne
    @JoinColumn(name = "s_record", insertable = false, updatable = false)
    @JSONField(name = "s_record")
    private recordEntity record;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
    private List<pageEntity> pages;

    public sectionEntity() {
        this.pages = new ArrayList<>();
    }

    public sectionEntity(String s_id, String s_title, String s_date, String s_record) {
        this.s_id = s_id;
        this.s_title = s_title;
        this.s_date = s_date;
        this.s_record = s_record;
    }

    public recordEntity getRecord() {
        return record;
    }

    public void setRecord(recordEntity record) {
        this.record = record;
    }

    public List<pageEntity> getPages() {
        return pages;
    }

    public void setPages(List<pageEntity> pages) {
        this.pages = pages;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_title() {
        return s_title;
    }

    public void setS_title(String s_title) {
        this.s_title = s_title;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getS_record() {
        return s_record;
    }

    public void setS_record(String s_record) {
        this.s_record = s_record;
    }
}
