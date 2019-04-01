package gcu.mpd.myapp;

public class earthquake {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String gLat;
    private String gLong;

    public earthquake()
    {
        title = "";
        description = "";
        link  = "";
        pubDate = "";
        category = "";
        gLat = "";
        gLong = "";

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
        String temp;
        String ss[] = title.split(":", 2);
        String M[] = ss[1].split(":" ,2);

        String N[] = M[1].split(",",3);
        String name = N[0].concat(" " + N[1]);
        return name;
    }

    //Error here if theres no second part of the name it displays "NAME Fri"

    public String splitTitle(){
        {
            String temp;
            String ss[] = title.split(":", 2);
            String M[] = ss[1].split(":" ,2);
            String N[] = M[1].split(",",3);
            String name = N[0].concat(" " + N[1]);
            temp = "Location: " +  name + '\n' +
                    "Date: " + N[2] + '\n' +
                    "Mag: " + M[0] + '\n';
            return temp;
        }
    }

    @Override
    public String toString() {
        String temp;
        String ss[] = title.split(":", 2);
        String M[] = ss[1].split(":" ,2);
        String N[] = M[1].split(",",3);
        String name = N[0].concat(" " + N[1]);
        temp = "Location: " +  name + '\n' +
                "Date: " + N[2] + '\n' +
                "Mag: " + M[0] + '\n';
        return temp;
    }
}
