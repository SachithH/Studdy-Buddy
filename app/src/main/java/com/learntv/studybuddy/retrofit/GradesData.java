package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GradesData {
    @SerializedName("list")
    @Expose
    private List<GradesDataList> list = null;
    @SerializedName("titleS")
    @Expose
    private String titleS;
    @SerializedName("titleE")
    @Expose
    private String titleE;
    @SerializedName("type")
    @Expose
    private String type;

    public List<GradesDataList> getList() {
        return list;
    }

    public void setList(List<GradesDataList> list) {
        this.list = list;
    }

    public String getTitleS() {
        return titleS;
    }

    public void setTitleS(String titleS) {
        this.titleS = titleS;
    }

    public String getTitleE() {
        return titleE;
    }

    public void setTitleE(String titleE) {
        this.titleE = titleE;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
