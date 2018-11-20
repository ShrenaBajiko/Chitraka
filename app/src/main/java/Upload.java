
public class Upload {
    private String ImageUrl;
    private String Det;

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

}

