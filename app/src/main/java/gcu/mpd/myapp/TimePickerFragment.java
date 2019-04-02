package gcu.mpd.myapp;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;


public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    String time;
    private ArrayList<earthquake> ae;
    ArrayList<earthquake> Dearthquakes = new ArrayList<>();
    Double cHLa = 0.0;
    Double cLLa = 90.00;
    Double cHLo = -180.00;
    Double cLLo = 0.0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
            //Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }



    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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
    }



}
