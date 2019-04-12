package gcu.mpd.myapp;

/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Class for the Custom Search Results Adapter
 */
public class SearchResultsAdapter extends ArrayAdapter{

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<earthquake> earthquakeList;

    /**
     * Called when the search results adapter is called
     * @param context postion of earthquake for
     * @param resource int
     * @param earthquakeList arraylist of earthquake objects
     */
    public SearchResultsAdapter(Context context, int resource, List<earthquake> earthquakeList){
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.earthquakeList = earthquakeList;
    }

    /**
     * Gets size of earthquake list
     * @return size of earthquake list
     */
    @Override
    public int getCount() {
        return earthquakeList.size();
    }

    /**
     * Retreives each compontent from search results XML and appends attributes from each earthquake in the Arraylist appropriately
     * @param position postion
     * @param convertView view
     * @param parent parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(layoutResource, parent, false);

        TextView atr = (TextView) v.findViewById(R.id.atr);
        TextView title = (TextView) v.findViewById(R.id.name);
        TextView pub = (TextView) v.findViewById(R.id.pub);
        TextView lat = (TextView) v.findViewById(R.id.lat);
        TextView glong = (TextView) v.findViewById(R.id.glong);
        TextView mag = (TextView) v.findViewById(R.id.mag);
        TextView depth = (TextView) v.findViewById(R.id.depth);

        earthquake e = earthquakeList.get(position);
        atr.setText(e.getAtr());
        title.setText(e.getTitle());
        pub.setText(e.getPubDate());
        lat.setText(e.getgLat());
        glong.setText(e.getgLong());
        mag.setText(e.getMag());
        depth.setText(e.getDepth());
        return v;

    }
}
