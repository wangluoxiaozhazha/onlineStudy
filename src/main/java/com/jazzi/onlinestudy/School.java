package com.jazzi.onlinestudy;

public class School {
    private String name;
    private int imageId;

    School(String name,int  imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }

}
