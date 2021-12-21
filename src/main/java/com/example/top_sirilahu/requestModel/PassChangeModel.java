package com.example.top_sirilahu.requestModel;

import org.thymeleaf.util.StringUtils;

public class PassChangeModel
{
    String origin_password;
    String new_password;
    String con_password;

    public PassChangeModel() {
    }

    public PassChangeModel(String origin_password, String new_password, String con_password) {
        this.origin_password = origin_password;
        this.new_password = new_password;
        this.con_password = con_password;
    }

    public String getOrigin_password() {
        return origin_password;
    }

    public void setOrigin_password(String origin_password) {
        this.origin_password = origin_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getCon_password() {
        return con_password;
    }

    public void setCon_password(String con_password) {
        this.con_password = con_password;
    }

    //确认属性是否为空
    public boolean isNull()
    {
        if (StringUtils.isEmptyOrWhitespace(origin_password) || StringUtils.isEmptyOrWhitespace(con_password) || StringUtils.isEmptyOrWhitespace(new_password))
        {
            return true;
        }
        return false;
    }

    public boolean isPassSame()
    {
        if (new_password.equals(con_password))
        {
            return true;
        }

        return false;
    }
}
