package gcu.mpd.myapp;

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


import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;
    private MapView mapView;
    SupportMapFragment mapFragment;
    private ArrayList<earthquake> e;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("2", "Called");


        //Move this to on attach
        DatabaseHelper dbh = new DatabaseHelper(getContext());
       dbh.getWritableDatabase();
       e = dbh.returnall();
       

        View v = inflater.inflate(R.layout.fragment_map,null);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        //add this to var
        return v;


    }

    public void populateMap(ArrayList<earthquake> el) {
        for (earthquake e : el) {
            LatLng pos = new LatLng(Double.valueOf(e.getgLat()), Double.valueOf(e.getgLong()));
            mMap.addMarker(new MarkerOptions().position(pos).title(e.titleForMap()));

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng glasgow = new LatLng(55.86, -4.25);
          mMap.addMarker(new MarkerOptions().position(glasgow).title("Marker in Glasgow"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(glasgow));
        populateMap(e);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



}
