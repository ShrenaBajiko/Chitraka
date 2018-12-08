package com.example.dell.chitraka;


import android.widget.ImageButton;
import android.widget.TextView;

public class Upload {
        private String ImageUrl;
        private String Det;
        private String mKey;
        private int Likecount;
        private String Likebutton;



        public Upload()
        {


        }

        public Upload(String imageUrl,String detail,int Likecount)
        {
            if(detail.trim().equals("")){
                detail = "no description";
            }
            ImageUrl = imageUrl;
            Det = detail;
            this.Likecount=Likecount;

        }

    public Integer getLikecount() {
        return Likecount;
    }

    public void setLikecount(int Likecount) {
        this.Likecount = Likecount;
    }

    public String getLikebutton() {
        return Likebutton;
    }

    public void setLikebutton(String likebutton) {
        Likebutton = likebutton;
    }

    public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String imageUrl){
            ImageUrl = imageUrl;
        }
        public String getDet (){
            return Det;
        }

        public void  setDet(String detail){
            Det = detail;
        }
        public String getKey() {
            return mKey;
        }

        public void setKey(String key) {
            mKey = key;
        }

    }


