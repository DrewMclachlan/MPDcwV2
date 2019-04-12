package gcu.mpd.myapp;

/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


/**
 * Class for the Fragment that displays more info about a selected earthquake object from the listview
 */
public class MoreInfoFragment extends Fragment implements View.OnClickListener{

    private List<earthquake> earthquakeList;
    private TextView name;
    private TextView desc;
    private TextView link;
    private earthquake eobj;
    private TextView pub;
    private TextView cat;
    private TextView glong;
    private TextView glat;
    private Button vMap;
    DatabaseHelper dhb;
    earthquake e;


    /**
     * Called when the Fragments attaches gets the context from the parent and creates a new DatabaseHelper
     * @param context context of the current state
     */
    @Override
    public void onAttach(Context context) {
        dhb = new DatabaseHelper(context);
        super.onAttach(context);
    }

    /**
     * Called when the Fragment is loaded into the main view.
     * gets the position passed to this fragment from the HomeFragment when this fragment was initiated.
     * finds the earthquake object from the database corresponding to the position sent in.
     * then gets all the components defined in the XML and sets each component from an attribute from the earthquake object as well
     * as defining an onclick listener for a button
     * @param inflater layout inflater
     * @param container viewgroup container
     * @param savedInstanceState bundle
     * @return view
     */
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
        vMap = (Button) v.findViewById(R.id.vmap);


        name.setText(e.getTitle());
        desc.setText(e.getDescription());
        link.setText(e.getLink());
        cat.setText(e.getCategory());
        pub.setText(e.getPubDate());
        glong.setText(e.getgLong());
        glat.setText(e.getgLat());

        vMap.setOnClickListener(this);
        Log.e("Moreinfo", "More Info Displayed");
        return v;
    }

    /**
     * Called if the button in the view is clicked which then launches a new mapfragment 2, passes it the
     * earthquake object that is currently being displayed and sets the mapfragment as the current viewable fragment
     * @param view view
     */
    @Override
    public void onClick(View view) {
        MapFragment2 map2 = new MapFragment2();
        Bundle bundle = new Bundle();
        bundle.putParcelable("obj", e);
        map2.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, map2 )
                .commit();
    }


}
