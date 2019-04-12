package gcu.mpd.myapp;

/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for earthquake objects
 */
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

    /**
     * get searched for datatime
     * @return searchfordate time
     */
    public String getSearchdt() {
        return searchdt;
    }

    /**
     * set searched for datatime
     * @param searchdt searched for datetime
     */
    public void setSearchdt(String searchdt) {
        this.searchdt = searchdt;
    }

    private String searchdt;

    /**
     * empty constructor
     */
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

    /**
     * argument constructor
     * @param atitle title
     * @param adescription description
     * @param alink link
     * @param apubDate publication date
     * @param acategory earthquake category
     * @param agLat Latitude
     * @param agLong Longitude
     */
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


    /**
     * object constructor
     * @param e earthquake object
     */
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

    /**
     * get  search Attribute
     * @return search attribute
     */
    public String getAtr() {
        return atr;
    }

    /**
     * set search Attribute
     * @param atr Search Attribute
     */
    public void setAtr(String atr) {
        this.atr = atr;
    }

    /**
     * get Magnitude
     * @return Magnitude
     */
    public String getMag() {
        return mag;
    }

    /**
     * set Magnitude
     * @param mag Magnitude
     */
    public void setMag(String mag) {
        this.mag = mag;
    }

    /**
     * get Depth
     * @return Depth
     */
    public String getDepth() {
        return depth;
    }

    /**
     * Set Depth
     * @param depth Depth
     */
    public void setDepth(String depth) {
        this.depth = depth;
    }

    /**
     * Get Title
     * @return Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set Title
     * @param title Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get Description
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set Description
     * @param description Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get Link
     * @return Link
     */
    public String getLink() {
        return link;
    }

    /**
     * Set Link
     * @param link Link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * get Pub Date
     * @return Pub date
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Set Pub Date
     * @param pubDate Publication Date
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * Get Category
     * @return Category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set Category
     * @param category Earthquake Category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * get Latitude
     * @return Latitude
     */
    public String getgLat() {
        return gLat;
    }

    /**
     * Set Latitude
     * @param gLat Latitude
     */
    public void setgLat(String gLat) {
        this.gLat = gLat;
    }

    /**
     * Get Longitude
     * @return Longitude
     */
    public String getgLong() {
        return gLong;
    }

    /**
     * Set Longitude
     * @param gLong Longitude
     */
    public void setgLong(String gLong) {
        this.gLong = gLong;
    }


    /**
     * get the title for the map fragment
     * @return titleformap
     */
    public String titleForMap(){
        return title;
    }

    /**
     * To String
     * @return string valye of earthquake
     */
    @Override
    public String toString() {

       String temp =  atr + "\n" + "\n" + title + "\n" + pubDate + "\n"  + "Mag: " + mag + "  Depth: " + depth + "  Lat: " + gLat + "  Long: " + gLong;
        return temp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Converst an earthquake object into Parceable data
     * @param dest destination
     * @param flags flags
     */
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

    /**
     * Sets an earthquake object from Parcelable data
     * @param in parcel to be unpact
     */
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

    /**
     * Controlers for Parcelable operations
     */
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
