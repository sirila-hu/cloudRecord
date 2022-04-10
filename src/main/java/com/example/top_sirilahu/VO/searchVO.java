package com.example.top_sirilahu.VO;

import com.alibaba.fastjson.annotation.JSONField;

public class searchVO <T>
{
    @JSONField(name = "data")
    private T data;

    @JSONField(name = "to")
    private String to;

    @JSONField(name = "titlePath")
    private String titlePath;

    public searchVO(T data, String to) {
        this.data = data;
        this.to = to;
    }

    public searchVO(T data, String to, String titlePath) {
        this.data = data;
        this.to = to;
        this.titlePath = titlePath;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitlePath() {
        return titlePath;
    }

    public void setTitlePath(String titlePath) {
        this.titlePath = titlePath;
    }
}
