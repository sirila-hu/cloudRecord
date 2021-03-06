package com.example.top_sirilahu.jsonBody;

import com.alibaba.fastjson.annotation.JSONField;

public class VOJSON<T> {
    @JSONField(name = "status")
    private int status;

    @JSONField(name = "data")
    private T data;

    public VOJSON(int status, T data ) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
