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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Map Fragment for populating the Map with the search results
 */
public class MapFragment3 extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mapView;
    SupportMapFragment mapFragment;
    private ArrayList<earthquake> e;

    /**
     * Receives the ArrayList passed to the fragment from the Search Fragment and sets up the Googlemaps
     * @param inflater layoutInflater
     * @param container viewGroup container
     * @param savedInstanceState bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle extras = getArguments();
        e = extras.getParcelableArrayList("aL");
        View v = inflater.inflate(R.layout.fragment_map,null);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return v;


    }

    /**
     * Populates the Map with each earthquake from the search result
     * @param el
     */
    public void populateMap(ArrayList<earthquake> el) {
        for (earthquake e : el) {
            LatLng pos = new LatLng(Double.valueOf(e.getgLat()), Double.valueOf(e.getgLong()));
            mMap.addMarker(new MarkerOptions().position(pos).title(e.titleForMap()));

        }
    }

    /**
     * Sets the camera position and zoom
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



}
