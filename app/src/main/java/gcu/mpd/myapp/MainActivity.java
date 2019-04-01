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



    private ListView listApps;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Log.d(TAG, "OC: starting Asynctask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
       // Log.d(TAG, "OC: done");
       // listApps = (ListView) findViewById(R.id.xmlistview);
        mTextMessage = (TextView) findViewById(R.id.message);
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
                mTextMessage.setText(R.string.title_home);
                fragment = new HomeFragment();
                break;

            case R.id.navigation_dashboard:
                mTextMessage.setText(R.string.title_dashboard);
                fragment = new MapFragment();
                break;
            case R.id.navigation_notifications:

                mTextMessage.setText(R.string.title_notifications);
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


            //Create new thread for both these operations so they dont block the main thread.

          //  populateMap(xmlParse.getEarthquakeList());

           // addToDb(xmlParse.getEarthquakeList());


         //  ArrayAdapter<earthquake> arrayAdapter = new ArrayAdapter<>(
         //          MainActivity.this, R.layout.list_item, xmlParse.getEarthquakeList());
//            listApps.setAdapter(arrayAdapter);
             // s = the xml after the do in background methoded has downloaded
        }


       // private void addToDb(ArrayList<earthquake> e){
         //   DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
         //   dbh.getWritableDatabase();
         //   dbh.drop();
          //  for(earthquake o : e){
           //     String title = o.getTitle();
           //     String description = o.getDescription();
           //     String link = o.getLink();
            //    String  pubDate = o.getPubDate();
            //    String  category = o.getCategory();
            //    Double  gLat = Double.valueOf(o.getgLat());
             //   Double  gLong = Double.valueOf(o.getgLong());
             //   dbh.insert(title, description, link, pubDate, category, gLat, gLong);
           // }
       // }

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
