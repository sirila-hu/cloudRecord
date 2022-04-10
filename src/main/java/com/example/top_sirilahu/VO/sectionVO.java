package com.example.top_sirilahu.VO;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.sectionEntity;

import java.util.ArrayList;
import java.util.List;

public class sectionVO {

    @JSONField(name = "s_id")
    private String s_id;

    @JSONField(name = "s_title")
    private String s_title;

    @JSONField(name = "s_date")
    private String s_date;

    @JSONField(name = "s_record")
    private String s_record;

    @JSONField(name = "pages")
    private List<pageVO> pages;

    @JSONField(name = "isExpand")
    private boolean isExpand = false;

    public sectionVO() {
    }

    public sectionVO(sectionEntity section) {
        this.s_id = section.getS_id();
        this.s_title = section.getS_title();
        this.s_date = section.getS_date();
        this.s_record = section.getS_record();

        if (section.getPages() != null)
        {
            this.pages = new ArrayList<>();
            for (pageEntity page: section.getPages())
            {
                pages.add(new pageVO(page));
            }
        }
    }

    public static List<sectionVO> convertToVO(List<sectionEntity> entities)
    {
        List<sectionVO> VOS = new ArrayList<>();

        //遍历转换
        for (sectionEntity entity: entities)
        {
            VOS.add(new sectionVO(entity));
        }

        return VOS;
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

    public List<pageVO> getPages() {
        return pages;
    }

    public void setPages(List<pageVO> pages) {
        this.pages = pages;
    }

    public boolean isExpand() {
        return isExpand;
    }
}
