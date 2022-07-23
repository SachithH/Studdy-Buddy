package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VODResponseData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("grade")
    @Expose
    private Integer grade;
    @SerializedName("termS")
    @Expose
    private String termS;
    @SerializedName("episode")
    @Expose
    private Integer episode;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("shortDesc")
    @Expose
    private String shortDesc;
    @SerializedName("lesson")
    @Expose
    private Integer lesson;
    @SerializedName("subjectS")
    @Expose
    private String subjectS;
    @SerializedName("subjectE")
    @Expose
    private String subjectE;
    @SerializedName("syllabusE")
    @Expose
    private String syllabusE;
    @SerializedName("syllabusS")
    @Expose
    private String syllabusS;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("favoritedState")
    @Expose
    private String favoritedState;
    @SerializedName("completedState")
    @Expose
    private String completedState;
    @SerializedName("totalCorrectAnswers")
    @Expose
    private String totalCorrectAnswers;
    @SerializedName("totalQuestions")
    @Expose
    private String totalQuestions;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("thumb")
    @Expose
    private String thumb;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getTermS() {
        return termS;
    }

    public void setTermS(String termS) {
        this.termS = termS;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Integer getLesson() {
        return lesson;
    }

    public void setLesson(Integer lesson) {
        this.lesson = lesson;
    }

    public String getSubjectS() {
        return subjectS;
    }

    public void setSubjectS(String subjectS) {
        this.subjectS = subjectS;
    }

    public String getSubjectE() {
        return subjectE;
    }

    public void setSubjectE(String subjectE) {
        this.subjectE = subjectE;
    }

    public String getSyllabusE() {
        return syllabusE;
    }

    public void setSyllabusE(String syllabusE) {
        this.syllabusE = syllabusE;
    }

    public String getSyllabusS() {
        return syllabusS;
    }

    public void setSyllabusS(String syllabusS) {
        this.syllabusS = syllabusS;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getFavoritedState() {
        return favoritedState;
    }

    public void setFavoritedState(String favoritedState) {
        this.favoritedState = favoritedState;
    }

    public String getCompletedState() {
        return completedState;
    }

    public void setCompletedState(String completedState) {
        this.completedState = completedState;
    }

    public String getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public void setTotalCorrectAnswers(String totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
