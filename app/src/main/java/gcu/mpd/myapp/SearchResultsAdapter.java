package gcu.mpd.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class SearchResultsAdapter extends ArrayAdapter{

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<earthquake> earthquakeList;

    public SearchResultsAdapter(Context context, int resource, List<earthquake> earthquakeList){
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.earthquakeList = earthquakeList;
    }
    @Override
    public int getCount() {
        return earthquakeList.size();
    }

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
