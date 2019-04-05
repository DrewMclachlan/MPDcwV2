package gcu.mpd.myapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class XMLParse {
    private static final String TAG = "XMLParse";



    private ArrayList<earthquake> earthquakeList;

    //Constructor
    public XMLParse(){
        this.earthquakeList = new ArrayList<>();
    }

    public ArrayList<earthquake> getEarthquakeList() {
        return earthquakeList;
    }


    public boolean parse(String xmlData){
        boolean status = true;
        earthquake currentRecord = null;
        boolean inEntry = false;
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
                            inEntry = true;
                            currentRecord = new earthquake();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("item".equalsIgnoreCase(tagName)){
                                earthquakeList.add(currentRecord);
                                inEntry = false;
                            } else if("title".equalsIgnoreCase(tagName))
                            {
                                currentRecord.setTitle(textValue);
                            }else if("description".equalsIgnoreCase(tagName))
                            {
                                currentRecord.setDescription(textValue);
                            }else if("link".equalsIgnoreCase(tagName))
                            {
                                currentRecord.setLink(textValue);
                            }else if("pubDate".equalsIgnoreCase(tagName))
                            {
                                currentRecord.setPubDate(textValue);
                            }else if("category".equalsIgnoreCase(tagName))
                            {
                                currentRecord.setCategory(textValue);
                            }else if("lat".equalsIgnoreCase(tagName))
                            {
                                currentRecord.setgLat(textValue);
                            }else if("long".equalsIgnoreCase(tagName))
                            {
                                currentRecord.setgLong(textValue);
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

