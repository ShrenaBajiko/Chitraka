package com.example.dell.chitraka;


    public class Upload {
        private String ImageUrl;
        private String Det;
        private String mKey;

        public Upload()
        {

        }
        public Upload(String imageUrl,String detail)
        {
            if(detail.trim().equals("")){
                detail = "no description";
            }
            ImageUrl = imageUrl;
            Det = detail;

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


