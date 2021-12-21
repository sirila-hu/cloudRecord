package com.example.top_sirilahu.service;

import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.repository.pageRepository;
import com.example.top_sirilahu.repository.sectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class editService
{
    private sectionRepository sectionRepo;
    private pageRepository pageRepo;

    public editService() {
    }

    @Autowired
    public editService(sectionRepository sectionRepo, pageRepository pageRepo) {
        this.sectionRepo = sectionRepo;
        this.pageRepo = pageRepo;
    }

    //获取渲染项目
    public void getProcessItem(recordEntity record)
    {
        //获取选择笔记本下关联的所有分区
        List<sectionEntity> sections = sectionRepo.getSections(record.getR_id());
        //向每个分区填充其关联的页
        if (sections != null)
        {
            for(sectionEntity section : sections)
            {
                section.setPages(pageRepo.getPages(section.getS_id()));
            }
        }

        //将填充完成的分区集合注入到record实体中
        record.setSections(sections);
    }

    //添加分区
    public String addSection(sectionEntity section)
    {
        //生成分区编号
        int count = sectionRepo.countByS_record(section.getS_record());
        String s_id = String.format("%s-1",section.getS_record());
        if (count != 0)
        {
            sectionEntity lastSectoion = sectionRepo.getLastSection(section.getS_record());
            String[] code = lastSectoion.getS_id().split("-");
            s_id = String.format("%s-%d", section.getS_record(),Integer.parseInt(code[code.length - 1]) + 1);
        }

        section.setS_id(s_id);

        //保存section
        sectionRepo.saveSection(section);

        return s_id;
    }

    //修改分区
    public void editSection(String s_title, String s_id )
    {
        int updateRows = sectionRepo.editSection(s_title, s_id);
        if (updateRows <= 0)
        {
            throw new EmptyResultDataAccessException(updateRows);
        }
    }

    //删除分区
    public void delSection(String s_id)
    {
        sectionRepo.deleteById(s_id);
    }

    //添加页
    public void addPage(pageEntity page)
    {
        //生成分区编号
        int count = pageRepo.countByP_section(page.getP_section());
        String p_id = String.format("%s-1",page.getP_section());
        if (count != 0)
        {
            pageEntity lastPage = pageRepo.getLastpage(page.getP_section());
            String[] code = lastPage.getP_id().split("-");
            p_id = String.format("%s-%d", page.getP_section(),Integer.parseInt(code[code.length - 1]) + 1);
        }
        page.setP_id(p_id);
        pageRepo.savePage(page);
    }

    //修改页
    public void editPage(String p_title, String p_id)
    {
        int updateRows = pageRepo.editPage(p_title, p_id);
        if (updateRows <= 0)
        {
            throw new EmptyResultDataAccessException(updateRows);
        }
    }

    //删除页
    public void delPage(String p_id)
    {
        pageRepo.deleteById(p_id);
    }

}
