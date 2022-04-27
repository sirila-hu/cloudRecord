package com.example.top_sirilahu.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "tb_record")
public class recordEntity {
    //记录编号
    @Id
    @NotNull
    @Column(name = "r_id")
    private String r_id;

    //记录标题
    @Column(name = "r_name")
    private String r_name;

    //创建日期
    @Column(name = "r_date")
    private String r_date;

    //创建用户
    @Column(name = "r_creator")
    private long r_creator;

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY)
    List<sectionEntity> sections;

    public recordEntity() {
    }

    public List<sectionEntity> getSections() {
        return sections;
    }

    public void setSections(List<sectionEntity> sections) {
        this.sections = sections;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }

    public long getR_creator() {
        return r_creator;
    }

    public void setR_creator(long r_creator) {
        this.r_creator = r_creator;
    }

    public recordEntity(String r_id, String r_name, String r_date, long r_creator) {
        this.r_id = r_id;
        this.r_name = r_name;
        this.r_date = r_date;
        this.r_creator = r_creator;
    }
}
