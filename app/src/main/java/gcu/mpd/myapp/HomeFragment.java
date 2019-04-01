package gcu.mpd.myapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ListView listApps;
    Context thiscontext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        thiscontext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("1", "Called");
        thiscontext = container.getContext();
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
           View view = inflater.inflate(R.layout.fragment_home,null);
        listApps = (ListView) view.findViewById(R.id.xmlistview);

           return view;
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
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

            addToDb(xmlParse.getEarthquakeList());

            ArrayAdapter<earthquake> arrayAdapter = new ArrayAdapter<>(
                    thiscontext, R.layout.list_item, xmlParse.getEarthquakeList());
            listApps.setAdapter(arrayAdapter);

           //  s = the xml after the do in background methoded has downloaded
        }


         private void addToDb(ArrayList<earthquake> e){
           DatabaseHelper dbh = new DatabaseHelper(thiscontext);
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
          dbh.insert(title, description, link, pubDate, category, gLat, gLong);
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
