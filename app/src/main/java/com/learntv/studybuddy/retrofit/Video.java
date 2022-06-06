package com.learntv.studybuddy.retrofit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mcq")
    @Expose
    private List<Mcq> mcq = null;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Mcq> getMcq() {
        return mcq;
    }

    public void setMcq(List<Mcq> mcq) {
        this.mcq = mcq;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}