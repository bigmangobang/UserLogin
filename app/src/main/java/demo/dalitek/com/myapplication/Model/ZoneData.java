package demo.dalitek.com.myapplication.Model;

public class ZoneData {
    private String userName;                  //用户名
    private String zoneWord;                  //发布内容
    private String img;
    public ZoneData(String userName,String zoneWord){
        this.userName  = userName;
        this.zoneWord = zoneWord;
    }
    public ZoneData(String userName,String zoneWord,String img){
        this.userName  = userName;
        this.zoneWord = zoneWord;
        this.img = img;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getZoneWord() {
        return zoneWord;
    }

    public void setZoneWord(String zoneWord) {
        this.zoneWord = zoneWord;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
