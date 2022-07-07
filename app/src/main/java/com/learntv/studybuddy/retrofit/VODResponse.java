package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VODResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("data")
    @Expose
    private List<VODResponseData> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<VODResponseData> getData() {
        return data;
    }

    public void setData(List<VODResponseData> data) {
        this.data = data;
    }


}