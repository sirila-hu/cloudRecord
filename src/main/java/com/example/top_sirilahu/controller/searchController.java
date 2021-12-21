package com.example.top_sirilahu.controller;

import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.service.searchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Controller
public class searchController {
    searchService seaService;

    public searchController() {
    }

    @Autowired
    public searchController(searchService seaService) {
        this.seaService = seaService;
    }

    @RequestMapping("/recordProject/pageSearch")
    public String pageSearch(@RequestParam(value = "filed",required = false) String filed, Model model,  @AuthenticationPrincipal userEntity user)
    {
        if (!StringUtils.isEmptyOrWhitespace(filed))
        {
            List<pageEntity> pages =  seaService.pageSearch(user.getUID(), filed);
            if (pages.size() > 0)
            {
                seaService.unitSearch(pages, filed);
            }
            model.addAttribute("pages", pages);
        }
        model.addAttribute("filed", filed);
        return "recordResult";
    }

    @RequestMapping("/recordProject/sectionSearch")
    public String sectionSearch(@RequestParam(value = "filed",required = false) String filed, Model model,  @AuthenticationPrincipal userEntity user)
    {
        if (!StringUtils.isEmptyOrWhitespace(filed))
        {
            List<sectionEntity> sections =  seaService.sectionSearch(user.getUID(), filed);
            model.addAttribute("sections", sections);
        }
        model.addAttribute("filed", filed);
        return "recordResult";
    }

    @RequestMapping("/recordProject/recordSearch")
    public String recordSearch(@RequestParam(value = "filed",required = false) String filed, Model model,  @AuthenticationPrincipal userEntity user)
    {
        if (!StringUtils.isEmptyOrWhitespace(filed))
        {
            List<recordEntity> records =  seaService.recordSearch(user.getUID(), filed);
            model.addAttribute("records", records);
        }
        model.addAttribute("filed", filed);
        return "recordResult";
    }

    @RequestMapping("/recordProject/globalSearch")
    public String globalSearch(@RequestParam(value = "filed",required = false) String filed, Model model,  @AuthenticationPrincipal userEntity user)
    {
        if (!StringUtils.isEmptyOrWhitespace(filed))
        {
            List<recordEntity> records =  seaService.recordSearch(user.getUID(), filed);
            List<sectionEntity> sections =  seaService.sectionSearch(user.getUID(), filed);
            List<pageEntity> pages =  seaService.pageSearch(user.getUID(), filed);
            if (pages.size() > 0)
            {
                seaService.unitSearch(pages, filed);
            }
            model.addAttribute("pages", pages);
            model.addAttribute("sections", sections);
            model.addAttribute("records", records);
            model.addAttribute("filed", filed);
        }
        return "recordResult";
    }
}
