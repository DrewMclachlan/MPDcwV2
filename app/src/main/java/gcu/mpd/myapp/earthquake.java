package gcu.mpd.myapp;

import android.os.Parcel;
import android.os.Parcelable;

public class earthquake  implements Parcelable {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String gLat;
    private String gLong;

    private String mag;
    private String depth;
    private String atr;

    public String getSearchdt() {
        return searchdt;
    }

    public void setSearchdt(String searchdt) {
        this.searchdt = searchdt;
    }

    private String searchdt;

    public earthquake()
    {
        title = "";
        description = "";
        link  = "";
        pubDate = "";
        category = "";
        gLat = "";
        gLong = "";
        mag = "";
        depth = "";
        atr = "";
        searchdt = "";

    }
    public earthquake(String atitle, String adescription, String alink, String apubDate,
                      String acategory, String agLat, String agLong)
    {
        title = atitle;
        description = adescription;
        link  = alink;
        pubDate = apubDate;
        category = acategory;
        gLat = agLat;
        gLong = agLong;
    }

    public earthquake(earthquake e){
        title = e.title;
        description = e.description;
        link  = e.link;
        pubDate = e.pubDate;
        category = e.category;
        mag = e.mag;
        depth = e.depth;
        gLat = e.gLat;
        gLong = e.gLong;
    }

    public String getAtr() {
        return atr;
    }

    public void setAtr(String atr) {
        this.atr = atr;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getgLat() {
        return gLat;
    }

    public void setgLat(String gLat) {
        this.gLat = gLat;
    }

    public String getgLong() {
        return gLong;
    }

    public void setgLong(String gLong) {
        this.gLong = gLong;
    }



    public String titleForMap(){
        return title;
    }

    //Error here if theres no second part of the name it displays "NAME Fri"

    @Override
    public String toString() {

       String temp =  atr + "\n" + "\n" + title + "\n" + pubDate + "\n"  + "Mag: " + mag + "  Depth: " + depth + "  Lat: " + gLat + "  Long: " + gLong;
        return temp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.link);
        dest.writeString(this.pubDate);
        dest.writeString(this.category);
        dest.writeString(this.gLat);
        dest.writeString(this.gLong);
        dest.writeString(this.mag);
        dest.writeString(this.depth);
        dest.writeString(this.atr);
        dest.writeString(this.searchdt);
    }

    protected earthquake(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.link = in.readString();
        this.pubDate = in.readString();
        this.category = in.readString();
        this.gLat = in.readString();
        this.gLong = in.readString();
        this.mag = in.readString();
        this.depth = in.readString();
        this.atr = in.readString();
        this.searchdt = in.readString();
    }

    public static final Creator<earthquake> CREATOR = new Creator<earthquake>() {
        @Override
        public earthquake createFromParcel(Parcel source) {
            return new earthquake(source);
        }

        @Override
        public earthquake[] newArray(int size) {
            return new earthquake[size];
        }
    };
}
