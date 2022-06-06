package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class vod_VODResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errorcode")
    @Expose
    private int errorcode;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("list")
    @Expose
    private java.util.List<vod_List> vodList = null;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.util.List<vod_List> getList() {
        return vodList;
    }

    public void setList(java.util.List<vod_List> vodList) {
        this.vodList = vodList;
    }

}
