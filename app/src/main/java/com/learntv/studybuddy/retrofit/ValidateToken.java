package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateToken {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("errorcode")
    @Expose
    private Integer errorcode;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

}