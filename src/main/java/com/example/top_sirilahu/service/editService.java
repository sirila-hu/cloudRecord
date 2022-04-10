package com.example.top_sirilahu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.top_sirilahu.VO.pageVO;
import com.example.top_sirilahu.VO.recordVO;
import com.example.top_sirilahu.VO.sectionVO;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.jsonBody.VOJSON;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.repository.pageRepository;
import com.example.top_sirilahu.repository.sectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class editService {
    private sectionRepository sectionRepo;
    private pageRepository pageRepo;
    private recordService recordSe;

    private pageFileService pageFileSe;

    public editService() {
    }

    @Autowired
    public editService(sectionRepository sectionRepo, pageRepository pageRepo, recordService recordSe, pageFileService pageFileSe) {
        this.sectionRepo = sectionRepo;
        this.pageRepo = pageRepo;
        this.recordSe = recordSe;
        this.pageFileSe = pageFileSe;
    }


    //获取渲染项目
    public String getProcessItem(String r_id) {

        List<sectionEntity> sections = null;
        try {

            recordVO record = recordSe.getRecord(r_id);
            //获取选择笔记本下关联的所有分区
            sections = sectionRepo.getSections(record.getR_id());
            //向每个分区填充其关联的页
            if (sections != null) {
                for (sectionEntity section : sections) {
                    section.setPages(pageRepo.getPagesbyP_section(section.getS_id()));
                }
            }

            //将填充完成的分区集合注入到
            record.setSections(sectionVO.convertToVO(sections));

            return JSON.toJSONString(new VOJSON<recordVO>(0, record), SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            String msg = "查询发生错误";
            e.printStackTrace();
            if (!StringUtils.isEmptyOrWhitespace(e.getMessage())) {
                msg = e.getMessage();
            }
            return JSON.toJSONString(new statusJSON(1, msg));
        }
    }

    //添加分区
    @Transactional
    public String addSection(sectionEntity section) {
        //生成分区编号
        int count = sectionRepo.countByS_record(section.getS_record());
        String s_id = String.format("%s-1", section.getS_record());
        if (count != 0) {
            sectionEntity lastSectoion = sectionRepo.getLastSection(section.getS_record());
            String[] code = lastSectoion.getS_id().split("-");
            s_id = String.format("%s-%d", section.getS_record(), Integer.parseInt(code[code.length - 1]) + 1);
        }

        section.setS_id(s_id);

        //保存section
        sectionRepo.saveSection(section);

        return JSON.toJSONString(new VOJSON<sectionVO>(0, new sectionVO(section)));
    }

    //修改分区
    public void editSection(String s_title, String s_id) throws EmptyResultDataAccessException {
        int updateRows = sectionRepo.editSection(s_title, s_id);
        if (updateRows <= 0) {
            throw new EmptyResultDataAccessException(updateRows);
        }
    }

    //删除分区
    @Transactional(rollbackFor = Exception.class)
    public void delSection(String s_id) throws EmptyResultDataAccessException, IOException {
        //获取其下的所有页
        List<pageEntity> pages = pageRepo.getPagesbyP_section(s_id);

        //细粒度删除页
        if (!pages.isEmpty()) {
            //删除页文件逻辑
            for (pageEntity page : pages) {
                pageFileSe.delPage(page);
            }
        }
        //删除分区
        sectionRepo.deleteById(s_id);

    }

    //添加页
    @Transactional
    public String addPage(pageEntity page) {
        //生成分区编号
        int count = pageRepo.countByP_section(page.getP_section());
        String p_id = String.format("%s-1", page.getP_section());
        if (count != 0) {
            pageEntity lastPage = pageRepo.getLastpage(page.getP_section());
            String[] code = lastPage.getP_id().split("-");
            p_id = String.format("%s-%d", page.getP_section(), Integer.parseInt(code[code.length - 1]) + 1);
        }
        page.setP_id(p_id);

        pageRepo.savePage(page);

        return JSON.toJSONString(new VOJSON<>(0, new pageVO(page)), SerializerFeature.WriteMapNullValue);
    }

    //修改页
    @Transactional
    public void editPage(String p_title, String p_id) {
        int updateRows = pageRepo.editPage(p_title, p_id);
        if (updateRows <= 0) {
            throw new EmptyResultDataAccessException(updateRows);
        }
    }

    //删除页
    public void delPage(String p_id) throws IOException {
        Optional<pageEntity> optional = pageRepo.findById(p_id);

        //删除页并删除页文件
        pageFileSe.delPage(optional.get());
    }

}
