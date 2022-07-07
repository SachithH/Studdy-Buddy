package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LessonResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("data")
    @Expose
    private List<LessonData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Error getErrors() {
        return error;
    }

    public void setErrors(Error error) {
        this.error = error;
    }

    public List<LessonData> getData() {
        return data;
    }

    public void setData(List<LessonData> data) {
        this.data = data;
    }


}
