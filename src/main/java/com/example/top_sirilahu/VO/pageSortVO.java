package com.example.top_sirilahu.VO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class pageSortVO {
    //请求页数
    @NotNull(message = "页数选择异常")
    private int page = 1;

    //排序关键字
    @NotBlank(message = "排序参数设置异常")
    private String orderField = "r_date";

    //排列方式
    @NotNull
    private boolean isASC;

    public pageSortVO() {
    }

    public pageSortVO(int page, String orderField, boolean isASC) {
        this.page = page;
        this.orderField = orderField;
        this.isASC = isASC;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public boolean isASC() {
        return isASC;
    }

    public void setIsASC(boolean ASC) {
        isASC = ASC;
    }


}
