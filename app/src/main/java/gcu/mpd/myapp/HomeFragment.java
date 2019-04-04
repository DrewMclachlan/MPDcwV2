package gcu.mpd.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    ArrayList<earthquake> eal;
    String s = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        thiscontext = context;
        DatabaseHelper dbh = new DatabaseHelper(context);
        eal = dbh.returnall();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("1", "Called");
        thiscontext = container.getContext();
        //if(getArguments().getString("key2") != null){
        //    s = getArguments().getString("key2");
        //}
        View view = inflater.inflate(R.layout.fragment_home,null);
        listApps = (ListView) view.findViewById(R.id.xmlistview);



       // ArrayAdapter<earthquake> arrayAdapter = new ArrayAdapter<>(
            //    thiscontext, R.layout.list_item, eal);
       // listApps.setAdapter(arrayAdapter);

        ListAdapter eAdapter = new ListAdapter(thiscontext, R.layout.list_item, eal);
        listApps.setAdapter(eAdapter);

        listApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoreInfoAdapter mia = new MoreInfoAdapter();
                Bundle bundle = new Bundle();
                bundle.putInt("key", position);
                mia.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mia)
                        .commit();
            }


        });


        return view;
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }



}
