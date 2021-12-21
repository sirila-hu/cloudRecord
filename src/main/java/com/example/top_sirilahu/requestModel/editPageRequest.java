package com.example.top_sirilahu.requestModel;

import javax.validation.constraints.NotBlank;

public class editPageRequest
{
    @NotBlank(message = "请从正确路径访问")
    private String p_id;

    @NotBlank(message = "标题不能为空")
    private String p_title;

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }
}
