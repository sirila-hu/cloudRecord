package com.example.top_sirilahu.jsonBody;

import com.alibaba.fastjson.annotation.JSONField;

public class statusJSON
{
    @JSONField(name = "status")
    private int status;

    @JSONField(name = "message")
    private String message;

    @JSONField(name = "data")
    private String data;

    public statusJSON(int status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public statusJSON(int status, String message) {
        this.status = status;
        this.message = message;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
