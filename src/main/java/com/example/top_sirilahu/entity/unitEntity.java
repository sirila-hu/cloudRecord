package com.example.top_sirilahu.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_unit")
public class unitEntity
{
    @Id
    @Column(name = "u_id")
    private String u_id;

    @Column(name = "u_title")
    private String u_title;

    @Column(name = "u_content")
    private String u_content;

    @Column(name = "u_img")
    private String  u_img;

    @Column(name = "u_date")
    private String u_date;

    @Column(name = "u_page")
    @NotBlank(message = "请从正确路径访问")
    private String u_page;

    @ManyToOne
    @JoinColumn(name = "u_page",insertable = false, updatable = false)
    private pageEntity page;

    public unitEntity() {
    }

    public unitEntity(String u_id, String u_title, String u_content, String u_img, String u_date, String u_page) {
        this.u_id = u_id;
        this.u_title = u_title;
        this.u_content = u_content;
        this.u_img = u_img;
        this.u_date = u_date;
        this.u_page = u_page;
    }

    public pageEntity getPage() {
        return page;
    }

    public void setPage(pageEntity page) {
        this.page = page;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_title() {
        return u_title;
    }

    public void setU_title(String u_title) {
        this.u_title = u_title;
    }

    public String getU_content() {
        return u_content;
    }

    public void setU_content(String u_content) {
        this.u_content = u_content;
    }

    public String getU_img() {
        return u_img;
    }

    public void setU_img(String u_img) {
        this.u_img = u_img;
    }

    public String getU_date() {
        return u_date;
    }

    public void setU_date(String u_date) {
        this.u_date = u_date;
    }

    public String getU_page() {
        return u_page;
    }

    public void setU_page(String u_page) {
        this.u_page = u_page;
    }
}
