package com.example.dell.chitraka;


import android.net.Uri;
import android.widget.ImageButton;
import android.widget.TextView;

public class Upload {
        private String ImageUrl;
        private String Det;
        private String mKey;
        private int Likecount;
        private String Likebutton;
        private String Username;

    public Upload(String s, String s1, int i) {
    }
    public Upload() {
    }

    public Upload(String s, String test) {
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String name) {
        Username = name;
    }




        public Upload(String imageUrl,String detail,int Likecount,String username)
        {
            if(detail.trim().equals("")){
                detail = "no description";
            }
            ImageUrl = imageUrl;
            Det = detail;
            this.Likecount=Likecount;
            Username=username;
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


