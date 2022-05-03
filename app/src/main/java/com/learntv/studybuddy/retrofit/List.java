package com.learntv.studybuddy.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("quality")
    @Expose
    private Quality quality;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("imageSize")
    @Expose
    private String imageSize;
    @SerializedName("english_desc")
    @Expose
    private String englishDesc;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("sinhala_desc")
    @Expose
    private String sinhalaDesc;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("image")
    @Expose
    private String image;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getEnglishDesc() {
        return englishDesc;
    }

    public void setEnglishDesc(String englishDesc) {
        this.englishDesc = englishDesc;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSinhalaDesc() {
        return sinhalaDesc;
    }

    public void setSinhalaDesc(String sinhalaDesc) {
        this.sinhalaDesc = sinhalaDesc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}