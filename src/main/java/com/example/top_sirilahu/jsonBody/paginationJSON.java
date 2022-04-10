package com.example.top_sirilahu.jsonBody;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class paginationJSON {
    @JSONField(name = "status")
    private int status;

    @JSONField(name = "pageCount")
    private int pageCount;

    @JSONField(name = "page")
    private int page;

    @JSONField(name = "pagination")
    private List pagination;

    public paginationJSON(int status, int pageCount, int page, List pagination) {
        this.status = status;
        this.pageCount = pageCount;
        this.page = page;
        this.pagination = pagination;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List getPagination() {
        return pagination;
    }

    public void setPagination(List pagination) {
        this.pagination = pagination;
    }
}
