package gcu.mpd.myapp;

/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


/**
 *
 * Custom Adapter Class to display items in a list view
 */
public class ListAdapter extends ArrayAdapter {

   private final int layoutResource;
   private final LayoutInflater layoutInflater;
   private List<earthquake> earthquakeList;

    /**
     * Constructor
     * @param context context of the current state
     * @param resource resource
     * @param earthquakeList current list of earthquake objects
     */
   public ListAdapter(Context context, int resource, List<earthquake> earthquakeList){
       super(context, resource);
       this.layoutResource = resource;
       this.layoutInflater = LayoutInflater.from(context);
       this.earthquakeList = earthquakeList;
   }

    /**
     * Gets size of the earthquakeList
     * @return size of earthquake list
     */
   @Override
    public int getCount() {
        return earthquakeList.size();
    }

    /**
     * Finds each Attribute of defined in the XML
     * For each object in the earthquake list sets the Title, Pub Date and Sets the Mag
     * Colour codes the Mag between Green, Orange and Red depending on severity
     * @param position postion
     * @param convertView view
     * @param parent parent of view
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(layoutResource, parent, false);
        TextView title = (TextView) v.findViewById(R.id.name);
        TextView pub = (TextView) v.findViewById(R.id.pubdate);
        TextView mag = (TextView) v.findViewById(R.id.mag);
        earthquake e = earthquakeList.get(position);
        title.setText(e.getTitle());
        pub.setText(e.getPubDate());
        if(Double.parseDouble(e.getMag()) > 1.8){
            ((TextView) v.findViewById(R.id.mag)).setTextColor(Color.RED);
        }else if(Double.parseDouble(e.getMag()) > 1.0){
            ((TextView) v.findViewById(R.id.mag)).setTextColor(Color.parseColor("#FDA50F"));
        }else{
            ((TextView) v.findViewById(R.id.mag)).setTextColor(Color.GREEN);
        }
        mag.setText(e.getMag());
        return v;
    }
}
