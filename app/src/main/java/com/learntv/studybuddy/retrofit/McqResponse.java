package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McqResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("data")
    @Expose
    private McqData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Error getErrors() {
        return error;
    }

    public void setErrors(Error errors) {
        this.error = errors;
    }

    public McqData getData() {
        return data;
    }

    public void setData(McqData data) {
        this.data = data;
    }
}
