package com.example.demo.model;

import lombok.Data;

@Data
public class ReturnInfo {
    private Integer returnCode;
    private String errorMessage;

    public ReturnInfo() {
    }
}
