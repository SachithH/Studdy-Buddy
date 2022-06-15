package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignIn {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private SignInData data;
    @SerializedName("error")
    @Expose
    private Error error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SignInData getData() {
        return data;
    }

    public void setData(SignInData data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
