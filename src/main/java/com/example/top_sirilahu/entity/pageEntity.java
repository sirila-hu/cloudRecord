package com.example.top_sirilahu.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "tb_page")
public class pageEntity
{
    @Id
    @Column(name = "p_id")
    private String p_id;

    @Column(name = "p_title")
    @NotBlank(message = "标题不能为空")
    private String p_title;

    @Column(name = "p_date")
    private String p_date;

    @Column(name = "p_section")
    @NotBlank(message = "请从正确路径访问")
    private String p_section;

    @ManyToOne
    @JoinColumn(name = "p_section", insertable = false, updatable = false)
    private sectionEntity section;

    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
    private List<unitEntity> units;

    //用于前端渲染
    private boolean isActive = false;

    public pageEntity() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public pageEntity(String p_id, String p_title, String p_date, String p_section) {
        this.p_id = p_id;
        this.p_title = p_title;
        this.p_date = p_date;
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

    public List<unitEntity> getUnits() {
        return units;
    }

    public void setUnits(List<unitEntity> units) {
        this.units = units;
    }
}
