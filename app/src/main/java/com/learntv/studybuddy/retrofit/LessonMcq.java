package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LessonMcq {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("options")
    @Expose
    private List<LessonMcqOption> options = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<LessonMcqOption> getOptions() {
        return options;
    }

    public void setOptions(List<LessonMcqOption> options) {
        this.options = options;
    }
}
