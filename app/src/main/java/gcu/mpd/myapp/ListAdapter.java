package gcu.mpd.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter {

   private final int layoutResource;
   private final LayoutInflater layoutInflater;
   private List<earthquake> earthquakeList;

   public ListAdapter(Context context, int resource, List<earthquake> earthquakeList){
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
        TextView title = (TextView) v.findViewById(R.id.name);
        TextView pub = (TextView) v.findViewById(R.id.pubdate);
        TextView mag = (TextView) v.findViewById(R.id.mag);

        earthquake e = earthquakeList.get(position);
         title.setText(e.getTitle());
        pub.setText(e.getPubDate());
        mag.setText(e.getMag());

        return v;

    }
}
