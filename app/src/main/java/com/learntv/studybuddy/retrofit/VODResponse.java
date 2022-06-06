package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VODResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("imageSize")
    @Expose
    private String imageSize;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("termE")
    @Expose
    private String termE;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("english_desc")
    @Expose
    private String englishDesc;
    @SerializedName("episodeE")
    @Expose
    private String episodeE;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video")
    @Expose
    private Video video = null;
    @SerializedName("sinhala_desc")
    @Expose
    private String sinhalaDesc;
    @SerializedName("part")
    @Expose
    private String part;
    @SerializedName("lessonE")
    @Expose
    private String lessonE;
    @SerializedName("quality")
    @Expose
    private Quality quality;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("termS")
    @Expose
    private String termS;
    @SerializedName("lessonS")
    @Expose
    private String lessonS;

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTermE() {
        return termE;
    }

    public void setTermE(String termE) {
        this.termE = termE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEnglishDesc() {
        return englishDesc;
    }

    public void setEnglishDesc(String englishDesc) {
        this.englishDesc = englishDesc;
    }

    public String getEpisodeE() {
        return episodeE;
    }

    public void setEpisodeE(String episodeE) {
        this.episodeE = episodeE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Video getVideo() {return video;}

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getSinhalaDesc() {
        return sinhalaDesc;
    }

    public void setSinhalaDesc(String sinhalaDesc) {
        this.sinhalaDesc = sinhalaDesc;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getLessonE() {
        return lessonE;
    }

    public void setLessonE(String lessonE) {
        this.lessonE = lessonE;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTermS() {
        return termS;
    }

    public void setTermS(String termS) {
        this.termS = termS;
    }

    public String getLessonS() {
        return lessonS;
    }

    public void setLessonS(String lessonS) {
        this.lessonS = lessonS;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}