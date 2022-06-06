package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quality {

    @SerializedName("HD-small")
    @Expose
    private String hDSmall;

    public String getHDSmall() {
        return hDSmall;
    }

    public void setHDSmall(String hDSmall) {
        this.hDSmall = hDSmall;
    }

}