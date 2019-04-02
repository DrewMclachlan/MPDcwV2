package gcu.mpd.myapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Log.d(TAG, "OC: starting Asynctask");

       // Log.d(TAG, "OC: done");
       // listApps = (ListView) findViewById(R.id.xmlistview);

        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
        mTextMessage = (TextView) findViewById(R.id.message);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

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

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Log.d(TAG, "ONP" + s);
            //this is called after do in background is completed
            XMLParse xmlParse = new XMLParse();
            xmlParse.parse(s);
            ArrayList<earthquake> eal;
            eal = xmlParse.getEarthquakeList();
            for(earthquake e : eal)
            {
                //Set Title
                String S[] = e.getDescription().split(";", 3);
                String Title[] = S[1].split(":", 2);
                e.setTitle(Title[1].trim());

                // Log.e("Title", Title[1].trim());

                //Set Mag
                String S2[] = e.getDescription().split(";");
                String D[] = S2[4].split(":", 2);
                e.setMag(D[1]);

                // Log.e("Mag", D[1]);

                //Set Depth
                String temp2[] = e.getDescription().split(";");
                String D2[] = temp2[3].split(" ");
                e.setDepth(D2[2]);
                // Log.e("Depth", D2[2]);


            }

            //Create new thread for both these operations so they dont block the main thread.

            //  populateMap(xmlParse.getEarthquakeList());

            addToDb(xmlParse.getEarthquakeList());


            //  s = the xml after the do in background methoded has downloaded
        }


        private void addToDb(ArrayList<earthquake> e){
            DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
            dbh.getWritableDatabase();
            dbh.drop();
            for(earthquake o : e){
                String title = o.getTitle();
                String description = o.getDescription();
                String link = o.getLink();
                String  pubDate = o.getPubDate();
                String  category = o.getCategory();
                Double  gLat = Double.valueOf(o.getgLat());
                Double  gLong = Double.valueOf(o.getgLong());
                String  mag = o.getMag();
                String depth = o.getDepth();
                dbh.insert(title, description, link, pubDate, category, gLat, gLong, mag, depth);
            }
        }




        @Override
        protected String doInBackground(String... strings) {
            // Log.d(TAG, "DIB: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "DIB: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResults = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "Dxml: the response code was " + response);

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
                Log.e(TAG, "Dxml: Invlaid URl" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "Dxml: IOe reading data" + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "Dxml: Security Exception" + e.getMessage());
            }
            return null;

        }

    }






}
