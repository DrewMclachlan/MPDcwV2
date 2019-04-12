package gcu.mpd.myapp;

/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for the Main Activity of the appliaction
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    int count = 0;


    /**
     * Method called when the app is first launched. Creates a new instance of the database helper object and executes
     * the asynchronous task to download the RSS feed into the app by creating an instance of downloadData class and
     * preforming the execute method by passing in the URL of the data to be downloaded. Finally initialises the bottom navigation of the app
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper dbh = new DatabaseHelper(this);
        dbh.getWritableDatabase();
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);



    }

    /**
     * Called to load the fragment argument passed, using the SupportFragmentManger. Which then replaces
     * the current fragment within fragment_container in the activityMain XML with the fragment passed into the method
     * @param fragment fragment to be set
     * @return true if operation preformed
     */
    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Determines which fragment is passed to the loadfragment method
     * depending on what button on the bottom navigation
     * @param menuItem menu item clicked
     * @return call to load fragment method with argument of fragment selected by user
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_dashboard:
                fragment = new MapFragment();
                break;
            case R.id.navigation_notifications:
                fragment = new SearchFragment();
                break;
        }
        return loadFragment(fragment);
    }


    /**
     *A An Asynchronous task that is initialised in the oncreate method
     */
    private class DownloadData extends AsyncTask<String, Void, String> {
        /**
         *
         * Once the DownloadData.execute(url) this method is preformed on a new thread in the background
         * it passes the url the downloadXML method to be preformed and once complete returns the downloaded rssFeed
         * to the onPostExecute method
         * @param strings url
         * @return an unparsed RSSfeed
         */
        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e("Database", "Download XML error");
            }
            return rssFeed;
        }






        /**
         * executes once the Async Task has been preformed, takes in all the download Rssfeed. Creates a new xmlParse object
         * which the Rss feed is passed to, to be processed into earthquake objects contained within earthquake arraylist.
         * the Arraylist is then returned from the xmlparse object and each earthquake is further processed within the list
         * such as adding seperate Title, Magnatiude and Depth attributed. Finally adds the arrayList to the database and sets
         * then sets the homepage to a populated Homefragment essentially starting the application.
         * Calls the timer method to begin a count down before refresh.
         * @param RSSfeed string of xmlfeed
         */
        @Override
        protected void onPostExecute(String RSSfeed) {
            super.onPostExecute(RSSfeed);
            XMLParse xmlParse = new XMLParse();
            xmlParse.parse(RSSfeed);
            ArrayList<earthquake> eal;
            eal = xmlParse.getEarthquakeList();
            if (eal.isEmpty()) {
                Toast toast = Toast.makeText(MainActivity.this, "Could not get RSS Feed. Data from last time connected to internet still viewable", Toast.LENGTH_LONG);
                toast.show();
            } else {
                for (earthquake e : eal) {
                    //Set Title
                    String S[] = e.getDescription().split(";", 3);
                    String Title[] = S[1].split(":", 2);
                    e.setTitle(Title[1].trim());

                    //Set Mag
                    String S2[] = e.getDescription().split(";");
                    String D[] = S2[4].split(":", 2);
                    e.setMag(D[1]);

                    //Set Depth
                    String temp2[] = e.getDescription().split(";");
                    String D2[] = temp2[3].split(" ");
                    e.setDepth(D2[2]);

                }
                addToDb(xmlParse.getEarthquakeList());
                if (count == 0) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                }
                timer();
            }
        }

        /**
         * Begins a 10 minute countdown to preform the download data operation again acting as a earthquake data update
         */
        private void timer(){
            count = 1;
            Log.e("Timer", "Entered Timer");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e("Async", "Started");
                    DownloadData downloadData = new DownloadData();
                    downloadData.execute("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
                    MainActivity.this.runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                }
            }, 4*60*1000);
        }

        /**
         *adds each earthquake object to the database for each one in the Arraylist
         * @param e earthquake Arraylist
         */
        private void addToDb(ArrayList<earthquake> e){
            DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
            dbh.getWritableDatabase();
            dbh.drop();
            int id = 0;
            for(earthquake o : e){
                String title = o.getTitle();
                String description = o.getDescription();
                String link = o.getLink();
                String pubDate = o.getPubDate();
                String category = o.getCategory();
                Double gLat = Double.valueOf(o.getgLat());
                Double gLong = Double.valueOf(o.getgLong());
                String mag = o.getMag();
                String depth = o.getDepth();
                dbh.insert(id, title, description, link, pubDate, category, gLat, gLong, mag, depth);
                id++;
            }
            Log.e("Database", "Database Populated");
        }

        /**
         *  Downloads the XML from the specified url passed in in the arguments
         *  and collects the results into a single string.
         * @param urlPath url
         * @return earthquake Xmlfeed as stirng
         */
        private String downloadXML(String urlPath) {
            StringBuilder xmlResults = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResults.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResults.toString();
            } catch (MalformedURLException e) {
                Log.e("Invlaid URl", e.getMessage());
            } catch (IOException e) {
                Log.e("Dxml: IOe reading data", e.getMessage());
            } catch (SecurityException e) {
                Log.e("Dxml: Security Exception", e.getMessage());
            }
            return null;
        }

    }
}
