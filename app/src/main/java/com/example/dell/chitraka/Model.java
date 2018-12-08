package com.example.dell.chitraka;

public class Model
{
    String det,imageUrl;
    int likecount;

    public Model(int likecount) {
        this.likecount = likecount;
    }

    public Model()
    {

    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public String getDet() {
        return det;
    }

    public void setDet(String det) {
        this.det = det;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
