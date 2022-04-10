package com.example.top_sirilahu.jsonBody;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class searchJSON
{
    @JSONField(name = "status")
    private int status;

    @JSONField(name = "message")
    private String message;

    @JSONField(name = "sections")
    private List sections;

    @JSONField(name = "pages")
    private List pages;

    public searchJSON() {
    }

    public searchJSON(int status, String message, List sections, List pages) {
        this.status = status;
        this.message = message;
        this.sections = sections;
        this.pages = pages;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getSections() {
        return sections;
    }

    public void setSections(List sections) {
        this.sections = sections;
    }

    public List getPages() {
        return pages;
    }

    public void setPages(List pages) {
        this.pages = pages;
    }
}
