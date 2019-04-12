package gcu.mpd.myapp;
/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

/**
 * Map Fragment for main Map asset
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;
    private MapView mapView;
    SupportMapFragment mapFragment;
    private ArrayList<earthquake> e;


    /**
     * Pulls ArrayList of earthquake objects from the Database and
     * adds a google map fragment to this fragment using the fragment manger
     * @param inflater LayoutInflator
     * @param container viewgroup container
     * @param savedInstanceState bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DatabaseHelper dbh = new DatabaseHelper(getContext());
       dbh.getWritableDatabase();
       e = dbh.returnall();
        View v = inflater.inflate(R.layout.fragment_map,null);

        if(e == null){
            Log.e("Empty", "Empty");
            Toast toast = Toast.makeText(getContext(), "Could not get RSS Feed", Toast.LENGTH_SHORT);
            toast.show();
            return v;
        } else {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment == null) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mapFragment = SupportMapFragment.newInstance();
                ft.replace(R.id.map, mapFragment).commit();
            }
            mapFragment.getMapAsync(this);
            //add this to var
            return v;

        }
    }

    /**
     * Called once the googlemap fragment has loaded, sets a starting location and zoom for the camera position
     * calls the populateMap method
     * @param googleMap map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int zoom = 5;
        LatLng start = new LatLng(55.2, -3.0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
        populateMap(e);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    /**
     * Populates the map with the position of each earthquake in the database
     * @param el earthquake lisr
     */
    public void populateMap(ArrayList<earthquake> el) {
        for (earthquake e : el) {
            LatLng pos = new LatLng(Double.valueOf(e.getgLat()), Double.valueOf(e.getgLong()));
            mMap.addMarker(new MarkerOptions().position(pos).title(e.titleForMap()));

        }
    }



}
