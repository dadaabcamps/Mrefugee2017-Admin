package com.drcdadaab.mrefugeeadmin;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class NewsModel {
    String id, title, content, author, imageUrl;
    Boolean visible;
    Long timestamp;

    public NewsModel(){}

    public NewsModel(String id, String title, String content, String author, Long timestamp, Boolean visible, String imageUrl){
        this.id =id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.visible = visible;
        this.imageUrl = imageUrl;
    }

    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return this.content;
    }

    public String getAuthor(){
        return this.author;
    }

    public Long getTimestamp(){
        return this.timestamp;
    }

    public Boolean getVisible(){
        return this.visible;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }
}
