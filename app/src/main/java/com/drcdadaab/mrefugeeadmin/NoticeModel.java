package com.drcdadaab.mrefugeeadmin;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class NoticeModel {
    String id, title, description, source, author, agencyLogo;
    Boolean visible;
    Long expires, timestamp;

    public NoticeModel(){}

    public NoticeModel(String id, String title, String description, String source, String agencyLogo, String author, Long timestamp, Long expires, Boolean visible){
        this.id = id;
        this.title = title;
        this.description = description;
        this.source = source;
        this.agencyLogo = agencyLogo;
        this.author = author;
        this.timestamp = timestamp;
        this.expires = expires;
        this.visible = visible;
    }

    public String getTitle(){
        return this.title;
    }

    public String getId(){
        return this.id;
    }

    public String getDescription(){
        return this.description;
    }

    public String getSource(){
        return this.source;
    }

    public String getAgencyLogo(){
        return this.agencyLogo;
    }

    public Long getTimestamp(){
        return this.timestamp;
    }

    public Long getExpires(){
        return this.expires;
    }

    public Boolean getVisible(){
        return this.visible;
    }

}
