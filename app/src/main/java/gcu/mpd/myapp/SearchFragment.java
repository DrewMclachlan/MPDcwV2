package gcu.mpd.myapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private TextView mTv;
    private Button mBtn;
    private Button mTime;
    private Button mDateTime;
    private DatePickerDialog.OnDateSetListener mDateSetListner;
    private ArrayList<earthquake> ae;
    ArrayList<earthquake> Dearthquakes = new ArrayList<>();
    ArrayList<earthquake> earthquakesfinal = new ArrayList<>();
    Calendar c;
    DatePickerDialog dpd;
    Integer depth = 0;
    String time;
    Double cHLa = 0.0;
    Double cLLa = 90.00;
    Double cHLo = -180.00;
    Double cLLo = -0.0;
    private ListView listApps;
    Context thiscontext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        thiscontext = context;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("3", "Called");
        View v = inflater.inflate(R.layout.fragment_search,null);
        mBtn = (Button) v.findViewById(R.id.btnPick);
        mTime = (Button) v.findViewById(R.id.time);
        mDateTime = (Button) v.findViewById(R.id.datetime);
        listApps = (ListView) v.findViewById(R.id.xmlistview2);
        mBtn.setOnClickListener(this);
        mTime.setOnClickListener(this);
        mDateTime.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPick:
                showDatePickerDialog(v);
                break;
            case R.id.time:
                showTimePickerDialog(v);
                break;
            case R.id.datetime:
                showDialog(v);
                break;
        }
    }
    public void showTimePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);;
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        ae = dbh.returnall();
        for (earthquake e : ae) {
            String temp = e.getPubDate();
            String M[] = temp.split(" ", 5);
            String T[] = M[4].split(":",3);
            time = T[0] + T[1];
            Log.e("time", time);
            e.setPubDate(time);
        }
        TimePickerDialog timeDialog= new TimePickerDialog(getActivity(), timePickerListener, hour, minute, false);
        timeDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                Log.e("hour", String.valueOf(hourOfDay));
                Log.e("min", String.valueOf(minute));

                String h = String.valueOf(hourOfDay);
                if (h.length() == 1) {
                    h = "0" + h;
                }
                String m = String.valueOf(minute);
                if (m.length() == 1) {
                    m = "0" + m;
                }

                String t = h + m;
                timecalc(t);
            }


            public void timecalc(String t){
                Double hm = -1.0;
                Integer counter = 0;
                Integer hd = 0;

                Integer pos = null;
                Integer pos2 = null;
                Integer pos3 = null;
                Integer pos4 = null;
                Integer pos5 = null;
                Integer pos6 = null;

                for(earthquake e: ae){

                    //This whole section needs to add features that display more than one earthwake
                    //if they match e.g both have the same depth
                    //  Log.e("1", e.getPubDate());
                    // Log.e("2", t);
                    if(e.getPubDate().equals(time)){
                        //M
                        Dearthquakes.add(e);
                        String D[] = e.getDescription().split(";");
                        String M[] = D[4].split(":", 2);
                        //  Log.e("4", M[1]);
                        Double m2 = Double.valueOf(M[1]);
                        // Log.e("POSTconvert", String.valueOf(m2));
                        if(m2 > hm){
                            hm = m2;
                            pos = counter;
                        }

                        //D
                        //Make this a value in the earthquake object as well as mag
                        String temp2[] = e.getDescription().split(";");
                        String D2[] = temp2[3].split(" ");
                        Log.e("split", D2[2]);
                        if(Integer.valueOf(D2[2]) > hd){
                            hd = Integer.valueOf(D2[2]);
                            pos2 = counter;
                        }

                        //Lat

                        if(Double.valueOf(e.getgLat()) > cHLa){
                            cHLa = Double.valueOf(e.getgLat());
                            pos3 = counter;
                        }
                        if(Double.valueOf(e.getgLat()) < cLLa) {
                            cLLa = Double.valueOf(e.getgLat());
                            pos4 = counter;
                        }


                        Log.e("longval", e.getgLong());
                        Log.e("valtobeat", cLLo.toString());
                        //Long
                        if(Double.valueOf(e.getgLong()) > cHLo){
                            Log.e("entered", "1");
                            cHLo = Double.valueOf(e.getgLong());
                            pos5 = counter;
                        }
                        if(Double.valueOf(e.getgLong()) < cLLo) {
                            Log.e("entered", "2");
                            cLLo = Double.valueOf(e.getgLong());
                            pos6 = counter;
                        }
                        counter++;
                    }

                }
                for(earthquake e: Dearthquakes){
                    Log.e("debug", e.getDescription());
                }
                //DISPLAY THIS!!!
                Log.e("HIGHESTMAG", Dearthquakes.get(pos).getDescription());
                Log.e("HIGHESTDEPTh", Dearthquakes.get(pos2).getDescription());
                Log.e("NORTH", Dearthquakes.get(pos3).getDescription());
                Log.e("SOUTH", Dearthquakes.get(pos4).getDescription());
                Log.e("WEST", Dearthquakes.get(pos5).getDescription());
                Log.e("EAST", Dearthquakes.get(pos6).getDescription());

                earthquakesfinal.add(Dearthquakes.get(pos));
                earthquakesfinal.add(Dearthquakes.get(pos2));
                earthquakesfinal.add(Dearthquakes.get(pos3));
                earthquakesfinal.add(Dearthquakes.get(pos4));
                earthquakesfinal.add(Dearthquakes.get(pos5));
                earthquakesfinal.add(Dearthquakes.get(pos6));


                ArrayAdapter<earthquake> arrayAdapter = new ArrayAdapter<>(
                        thiscontext, R.layout.list_item, earthquakesfinal);
                listApps.setAdapter(arrayAdapter);

            }



        };











    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        ae = dbh.returnall();
        for (earthquake e : ae) {
            String temp = e.getPubDate();
            String M[] = temp.split(" ", 5);
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
                Calendar cal = Calendar.getInstance();
                cal.setTime(inputFormat.parse(M[2]));
                SimpleDateFormat outputFormat = new SimpleDateFormat("MM");
                String s = outputFormat.format(cal.getTime());
                temp = M[1] + s + M[3];
                e.setPubDate(temp);
            } catch (java.text.ParseException e2) {

            }
        }
        DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth, mDay);
        dateDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dateYouChoosed = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            String m = String.valueOf(monthOfYear + 1);
            if (m.length() == 1) {
                m = "0" + m;
            }
            String d = String.valueOf(dayOfMonth);
            if (d.length() == 1) {
                d = "0" + d;
            }
            String t = d+m+year;
            datecalc(t);
        }

    };
    public void datecalc(String t){
        Double hm = -1.0;
        Integer counter = 0;
        Integer hd = 0;

        Integer pos = null;
        Integer pos2 = null;
        Integer pos3 = null;
        Integer pos4 = null;
        Integer pos5 = null;
        Integer pos6 = null;

        for(earthquake e: ae){
            if(e.getPubDate().equals(t)){
                Log.e("1", "Im here");
                //M
                Dearthquakes.add(e);
                String D[] = e.getDescription().split(";");
                String M[] = D[4].split(":", 2);
                //  Log.e("4", M[1]);
                Double m2 = Double.valueOf(M[1]);
                // Log.e("POSTconvert", String.valueOf(m2));
                if(m2 > hm){
                    hm = m2;
                    pos = counter;
                }
                //D
                //Make this a value in the earthquake object as well as mag
                String temp2[] = e.getDescription().split(";");
                String D2[] = temp2[3].split(" ");
                Log.e("split", D2[2]);
                if(Integer.valueOf(D2[2]) > hd){
                    hd = Integer.valueOf(D2[2]);
                    pos2 = counter;
                }
                //Lat
                if(Double.valueOf(e.getgLat()) > cHLa){
                    cHLa = Double.valueOf(e.getgLat());
                    pos3 = counter;
                }
                if(Double.valueOf(e.getgLat()) < cLLa) {
                    cLLa = Double.valueOf(e.getgLat());
                    pos4 = counter;
                }



                //Long
                if(Double.valueOf(e.getgLong()) > cHLo) {
                    cHLo = Double.valueOf(e.getgLong());
                    pos5 = counter;
                }
                if(Double.valueOf(e.getgLong()) < cLLo) {
                    cLLo = Double.valueOf(e.getgLong());
                    pos6 = counter;
                }
                counter++;
            }
        }
        for(earthquake e: Dearthquakes){
            Log.e("debug", e.getDescription());
        }
        //DISPLAY THIS!!!
        Log.e("HIGHESTMAG", Dearthquakes.get(pos).getDescription());
        Log.e("HIGHESTDEPTh", Dearthquakes.get(pos2).getDescription());
        Log.e("NORTH", Dearthquakes.get(pos3).getDescription());
        Log.e("SOUTH", Dearthquakes.get(pos4).getDescription());
        Log.e("WEST", Dearthquakes.get(pos5).getDescription());
        Log.e("EAST", Dearthquakes.get(pos6).getDescription());


        earthquakesfinal.add(Dearthquakes.get(pos));
        earthquakesfinal.add(Dearthquakes.get(pos2));
        earthquakesfinal.add(Dearthquakes.get(pos3));
        earthquakesfinal.add(Dearthquakes.get(pos4));
        earthquakesfinal.add(Dearthquakes.get(pos5));
        earthquakesfinal.add(Dearthquakes.get(pos6));


        ArrayAdapter<earthquake> arrayAdapter = new ArrayAdapter<>(
                thiscontext, R.layout.list_item, earthquakesfinal);
        listApps.setAdapter(arrayAdapter);


    }


    public void showDialog(View v) {
        DialogFragment newFragment2 = new TimePickerFragment();
        newFragment2.show(getFragmentManager(), "timePicker");
    }






}
