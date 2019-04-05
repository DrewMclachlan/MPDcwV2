package gcu.mpd.myapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ListView earthquakelist;
    Context thiscontext;
    ArrayList<earthquake> eal;
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
        thiscontext = container.getContext();

        View view = inflater.inflate(R.layout.fragment_home,null);
        earthquakelist = (ListView) view.findViewById(R.id.xmlistview);


        ListAdapter eAdapter = new ListAdapter(thiscontext, R.layout.list_item, eal);
        earthquakelist.setAdapter(eAdapter);

        earthquakelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
