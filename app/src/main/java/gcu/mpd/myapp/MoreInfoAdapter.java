package gcu.mpd.myapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MoreInfoAdapter extends Fragment {

    private List<earthquake> earthquakeList;

    private TextView name;
    private TextView desc;
    private TextView link;
    private earthquake eobj;
    private TextView pub;
    private TextView cat;
    private TextView glong;
    private TextView glat;
    DatabaseHelper dhb;
    earthquake e;


    @Override
    public void onAttach(Context context) {
        dhb = new DatabaseHelper(context);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.more_info,null);
        dhb.getReadableDatabase();
        int i = getArguments().getInt("key");
        Log.e("number", String.valueOf(i));
        e = dhb.getObjById(i);
        name = (TextView) v.findViewById(R.id.name);
        desc = (TextView) v.findViewById(R.id.desc);
        link = (TextView) v.findViewById(R.id.link);
        cat = (TextView) v.findViewById(R.id.cat);
        pub = (TextView) v.findViewById(R.id.pub);
        glong = (TextView) v.findViewById(R.id.glong);
        glat = (TextView) v.findViewById(R.id.glat);

        name.setText(e.getTitle());
        desc.setText(e.getDescription());
        link.setText(e.getLink());
        cat.setText(e.getCategory());
        pub.setText(e.getPubDate());
        glong.setText(e.getgLong());
        glat.setText(e.getgLat());

        return v;
    }
}
