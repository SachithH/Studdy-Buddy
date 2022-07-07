package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LessonData {
    @SerializedName("videoId")
    @Expose
    private Integer videoId;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("shortDesc")
    @Expose
    private String shortDesc;
    @SerializedName("longDesc")
    @Expose
    private String longDesc;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("syllabus")
    @Expose
    private String syllabus;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("videoUrls")
    @Expose
    private List<LessonVideoUrl> videoUrls = null;
    @SerializedName("mcq")
    @Expose
    private List<LessonMcq> mcq = null;

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
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

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<LessonVideoUrl> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(List<LessonVideoUrl> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public List<LessonMcq> getMcq() {
        return mcq;
    }

    public void setMcq(List<LessonMcq> mcq) {
        this.mcq = mcq;
    }
}
