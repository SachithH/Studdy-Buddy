package com.learntv.studybuddy.retrofit.RequestPojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MCQAnswerRequest {
    @SerializedName("videoId")
    @Expose
    private Integer videoId;
    @SerializedName("mcq")
    @Expose
    private List<MCQAnswerRequestMcq> mcq = null;

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public List<MCQAnswerRequestMcq> getMcq() {
        return mcq;
    }

    public void setMcq(List<MCQAnswerRequestMcq> mcq) {
        this.mcq = mcq;
    }
}
