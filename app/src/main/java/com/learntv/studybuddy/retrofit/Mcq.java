package com.learntv.studybuddy.retrofit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mcq {

    @SerializedName("qid")
    @Expose
    private String qid;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;
    @SerializedName("currectAnswerId")
    @Expose
    private String currectAnswerId;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getCurrectAnswerId() {
        return currectAnswerId;
    }

    public void setCurrectAnswerId(String currectAnswerId) {
        this.currectAnswerId = currectAnswerId;
    }

}
