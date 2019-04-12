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

/**
 * Map Fragment for displaying a single earthquake on the map
 */
public class MapFragment2 extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mapView;
    SupportMapFragment mapFragment;
   earthquake e;

    /**
     * Receives the earthquake object passed to it and sets up the google map
     * @param inflater layout inflater
     * @param container viewgroup container
     * @param savedInstanceState bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle extras = getArguments();
        e = extras.getParcelable("obj");
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
     * Sets the camera postion and sets the location of a single earthquake
     * @param googleMap map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pos = new LatLng(Double.valueOf(e.getgLat()), Double.valueOf(e.getgLong()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(7.0f));
        mMap.addMarker(new MarkerOptions().position(pos).title(e.titleForMap()));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }



}
