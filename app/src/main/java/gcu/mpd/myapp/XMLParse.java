package gcu.mpd.myapp;
/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Class used to Parse the RSSfeed data from XML into readable data
 */
public class XMLParse {
    private ArrayList<earthquake> earthquakeList;

    /**
     * Constructor
     */
    public XMLParse(){
        this.earthquakeList = new ArrayList<>();
    }

    /**
     * Returns the data from the class
     * @return Arraylist of Earthquake Objects
     */
    public ArrayList<earthquake> getEarthquakeList() {
        return earthquakeList;
    }

    /**
     * Parsing logic.
     * Takes in the Rss feed as an argument and begins an XMLpullparser Factory. Runs a loop to continue until the end of the RSS document is reached.
     * Begins to iterate through the document looking for specific tags. Once an 'item' tag is found, it begins a new earthquake object.
     * then begins to check if the next tag is equal to certain tag, such as title, description and if so adds the value between the tags to the earthquake object
     * using the set parameter for that specific attribute. This continues through each tag within the item tags. Once the item tag is closed the constructed earthquake object
     * is added to an arraylist and the process continues for each item tag within the Rss feed until the end of the document.
     *
     * @param xmlData all xmldata in a string
     * @return populated ArrayList of earthquake object
     */
    public boolean parse(String xmlData){
        boolean status = true;
        earthquake currentEarthquake = null;
        boolean inItem = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        if("item".equalsIgnoreCase(tagName)){
                            inItem = true;
                            currentEarthquake = new earthquake();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(inItem){
                            if("item".equalsIgnoreCase(tagName)){
                                earthquakeList.add(currentEarthquake);
                                inItem = false;
                            } else if("title".equalsIgnoreCase(tagName))
                            {
                                currentEarthquake.setTitle(textValue);
                            }else if("description".equalsIgnoreCase(tagName))
                            {
                                currentEarthquake.setDescription(textValue);
                            }else if("link".equalsIgnoreCase(tagName))
                            {
                                currentEarthquake.setLink(textValue);
                            }else if("pubDate".equalsIgnoreCase(tagName))
                            {
                                currentEarthquake.setPubDate(textValue);
                            }else if("category".equalsIgnoreCase(tagName))
                            {
                                currentEarthquake.setCategory(textValue);
                            }else if("lat".equalsIgnoreCase(tagName))
                            {
                                currentEarthquake.setgLat(textValue);
                            }else if("long".equalsIgnoreCase(tagName))
                            {
                                currentEarthquake.setgLong(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }catch(Exception e){
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}

