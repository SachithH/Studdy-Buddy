package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GradesDataList {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nameE")
    @Expose
    private String nameE;
    @SerializedName("nameS")
    @Expose
    private String nameS;
    @SerializedName("thumb")
    @Expose
    private String thumb;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameE() {
        return nameE;
    }

    public void setNameE(String nameE) {
        this.nameE = nameE;
    }

    public String getNameS() {
        return nameS;
    }

    public void setNameS(String nameS) {
        this.nameS = nameS;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
