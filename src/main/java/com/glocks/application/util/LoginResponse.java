package com.glocks.application.util;

public class LoginResponse {
    private String response;
    private Integer statusCode;
    private String username;
    private Integer userId;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LoginResponse [response=" + response + ", statusCode=" + statusCode + ", username=" + username + ", userId=" + userId + "]";
    }
}