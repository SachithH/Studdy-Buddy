package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VODResponse {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("errorcode")
    @Expose
    private int errorcode;
    @SerializedName("list")
    @Expose
    private java.util.List<com.learntv.studybuddy.retrofit.List> list = null;

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

    public java.util.List<com.learntv.studybuddy.retrofit.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.learntv.studybuddy.retrofit.List> list) {
        this.list = list;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }
}

