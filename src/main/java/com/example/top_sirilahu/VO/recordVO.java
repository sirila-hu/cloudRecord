package com.example.top_sirilahu.VO;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;

import java.util.ArrayList;
import java.util.List;

public class recordVO {

    //记录编号
    @JSONField(name = "r_id")
    private String r_id;

    //记录标题
    @JSONField(name = "r_name")
    private String r_name;

    //创建日期
    @JSONField(name = "r_date")
    private String r_date;

    //创建用户
    @JSONField(name = "r_creator")
    private long r_creator;

    //链接的分区
    @JSONField(name = "sections")
    List<sectionVO> sections;

    public recordVO() {
    }

    public recordVO(String r_id, String r_name) {
        this.r_id = r_id;
        this.r_name = r_name;
    }

    public recordVO(recordEntity record) {
        this.r_id = record.getR_id();
        this.r_name = record.getR_name();
        this.r_date = record.getR_date();
        this.r_creator = record.getR_creator();

        //转换链接分区数据
        if (record.getSections() != null)
        {
            this.sections = new ArrayList<>();
            for (sectionEntity section : record.getSections())
            {
                sections.add(new sectionVO(section));
            }
        }
    }

    /**
     * convertToVO 批量转换实体对象
     * @param entities 待批量转换的实体对象
     * @return
     */
    public static List<recordVO> convertToVO(List<recordEntity> entities)
    {
        List<recordVO> recordVOS = new ArrayList<>();

        //遍历转换
        for (recordEntity entity: entities)
        {
            recordVOS.add(new recordVO(entity));
        }

        return recordVOS;
    }

    public List<sectionVO> getSections() {
        return sections;
    }

    public void setSections(List<sectionVO> sections) {
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
}
