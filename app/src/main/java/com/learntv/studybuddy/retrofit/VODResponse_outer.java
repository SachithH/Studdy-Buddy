package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VODResponse_outer {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("errorcode")
    @Expose
    private String errorcode;
    @SerializedName("vodresponse")
    @Expose
    private List<VODResponse> vodresponse = null;

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

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public List<VODResponse> getVODResponse() {
        return vodresponse;
    }

    public void setVideo(List<VODResponse> video) {
        this.vodresponse = video;
    }

}
