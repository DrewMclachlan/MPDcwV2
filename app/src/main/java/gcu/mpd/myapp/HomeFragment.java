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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Home Fragment Class
 */
public class HomeFragment extends Fragment {
    private ListView earthquakelist;
    Context thiscontext;
    ArrayList<earthquake> eal;


    /**
     * Method fires as soon as the fragment attaches, is first method to execute in the fragment lifecycle
     * gets the conext from the parent, in this case the Main Activity.
     * @param context context of the current state
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        thiscontext = context;
        DatabaseHelper dbh = new DatabaseHelper(thiscontext);
        eal = dbh.returnall();
    }

    /**
     * Called when the fragment is loaded into the view. Pulls an Arraylist from the database containing all the earthquake objects
     * this is then appended to a customer ArrayAdapter, ListAdapter within the ListView within the Homefragment XML.
     * Also sets an onclick listener to each item to within the ListAdapter. That when clicked collects the position
     * of the item clicked and creates a MoreInfoFragment, passes the position and sets the MoreinfoFragment to the current viewable fragment
     * @param inflater Layout inflater
     * @param container view container
     * @param savedInstanceState bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       DatabaseHelper dbh = new DatabaseHelper(thiscontext);
        eal = dbh.returnall();
        View view = inflater.inflate(R.layout.fragment_home,null);
        if(eal == null){
            Log.e("Empty", "Empty");
            Toast toast = Toast.makeText(thiscontext, "Could not get RSS Feed", Toast.LENGTH_SHORT);
            toast.show();
            return view;
        } else {
            earthquakelist = (ListView) view.findViewById(R.id.xmlistview);
            ListAdapter eAdapter = new ListAdapter(thiscontext, R.layout.list_item, eal);
            earthquakelist.setAdapter(eAdapter);
            earthquakelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Item", "Earthquake clicked");
                    MoreInfoFragment mia = new MoreInfoFragment();
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
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

}
