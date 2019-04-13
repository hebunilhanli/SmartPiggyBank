package com.example.hebun.piggybank;

public class Data {
    private int imageID;
    private String string;

    public Data(){

    }
    public Data(int imageID, String string){
        this.imageID = imageID;
        this.string = string;
    }

    public int getImageID() {
        return imageID;
    }

    public String getString() {
        return string;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public void setString(String string) {
        this.string = string;
    }
}

