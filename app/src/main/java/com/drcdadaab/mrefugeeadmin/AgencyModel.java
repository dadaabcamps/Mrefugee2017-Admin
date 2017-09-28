package com.drcdadaab.mrefugeeadmin;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AgencyModel {
    String id, name, code, imageUrl;
    public AgencyModel(){}

    public AgencyModel(String id, String name, String code, String imageUrl){
        this.id =id;
        this.name = name;
        this.code = code;
        this.imageUrl = imageUrl;
    }
    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getCode(){
        return this.code;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }
}