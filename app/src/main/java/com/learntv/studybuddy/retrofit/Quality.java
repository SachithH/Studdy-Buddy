package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quality {

    @SerializedName("HD-high")
    @Expose
    private String hDHigh;
    @SerializedName("HD-medium")
    @Expose
    private String hDMedium;
    @SerializedName("HD-small")
    @Expose
    private String hDSmall;
    @SerializedName("HD-xsmall")
    @Expose
    private String hDXsmall;

    public String getHDHigh() {
        return hDHigh;
    }

    public void setHDHigh(String hDHigh) {
        this.hDHigh = hDHigh;
    }

    public String getHDMedium() {
        return hDMedium;
    }

    public void setHDMedium(String hDMedium) {
        this.hDMedium = hDMedium;
    }

    public String getHDSmall() {
        return hDSmall;
    }

    public void setHDSmall(String hDSmall) {
        this.hDSmall = hDSmall;
    }

    public String getHDXsmall() {
        return hDXsmall;
    }

    public void setHDXsmall(String hDXsmall) {
        this.hDXsmall = hDXsmall;
    }

}
