package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McqData {
    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("question_id")
    @Expose
    private Integer questionId;
    @SerializedName("video_id")
    @Expose
    private Integer videoId;
    @SerializedName("earning")
    @Expose
    private String earning;
    @SerializedName("answer")
    @Expose
    private boolean answer;

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
