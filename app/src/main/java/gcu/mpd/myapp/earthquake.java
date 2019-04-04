package gcu.mpd.myapp;

public class earthquake {
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

       String temp =  atr + "\n" + "\n" + title + "\n"  + "Mag: " + mag + "  Depth: " + depth + "  Lat: " + gLat + "  Long: " + gLong;
        return temp;
    }
}
