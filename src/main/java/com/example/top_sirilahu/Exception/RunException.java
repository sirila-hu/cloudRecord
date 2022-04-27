package com.example.top_sirilahu.Exception;

public class RunException extends Exception
{
    private String resultJSON;

    public RunException(String message, String resultJSON) {
        super(message);
        this.resultJSON = resultJSON;
    }

    public String getResultJSON() {
        return resultJSON;
    }
}
