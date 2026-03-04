package com.ostad.product.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;

    public AppResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }

}