package gcu.mpd.myapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {


    private SearchView mSearchView;
    int count = 0;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                HomeFragment mia = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.getString("key2", s);
                mia.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mia)
                        .commit();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
            Log.e("Download", "Download Date Complete");
            if(count == 0) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
            timer();
        }

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
                }
            }, 2*60*1000);
        }


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
        }


        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e("Database", "Download XML error");
            }
            return rssFeed;
        }

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
