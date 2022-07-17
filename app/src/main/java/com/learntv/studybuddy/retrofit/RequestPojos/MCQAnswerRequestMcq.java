package com.learntv.studybuddy.retrofit.RequestPojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MCQAnswerRequestMcq {
    @SerializedName("questionId")
    @Expose
    private Integer questionId;
    @SerializedName("optionId")
    @Expose
    private Integer optionId;
    @SerializedName("startedAt")
    @Expose
    private String startedAt;
    @SerializedName("endedAt")
    @Expose
    private String endedAt;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }
}
