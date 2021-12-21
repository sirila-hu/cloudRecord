package com.example.top_sirilahu.requestModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//用于暂时存放修改分区请求参数
public class editSectionRequest
{
    @NotBlank(message = "请求参数不完整")
    private String s_id;

    @NotBlank(message = "标题不能为空")
    @Size(max = 45, message = "标题字数不能大于45")
    private String s_title;

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
}
