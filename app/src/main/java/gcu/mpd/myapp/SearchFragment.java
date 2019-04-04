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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private TextView mTv;
    private Button mBtn;
    private Button mTime;
    private Button nameb;
    private EditText name;
    private DatePickerDialog.OnDateSetListener mDateSetListner;
    private ArrayList<earthquake> ae;
    ArrayList<earthquake> Dearthquakes = new ArrayList<>();
    ArrayList<earthquake> earthquakesfinal = new ArrayList<>();
    Calendar c;
    DatePickerDialog dpd;
    Integer depth = 0;
    String time;
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
        nameb = (Button) v.findViewById(R.id.nameb);
        name = (EditText) v.findViewById(R.id.name);
        listApps = (ListView) v.findViewById(R.id.xmlistview2);
        mBtn.setOnClickListener(this);
        mTime.setOnClickListener(this);
        nameb.setOnClickListener(this);
        listApps.setAdapter(null);



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
           case R.id.nameb:
                searchByName();
               break;
        }
    }
    public void showTimePickerDialog(View v) {
        Dearthquakes.clear();
        earthquakesfinal.clear();
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
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
            listApps.setAdapter(null);
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

            public void timecalc(String t) {
                Double hm = -1.0;
                Integer counter = 0;
                Integer hd = 0;
                Double cHLa = 0.0;
                Double cLLa = 90.00;
                Double cHLo = -180.00;
                Double cLLo = 180.0;
                Integer pos = null;
                Integer pos2 = null;
                Integer pos3 = null;
                Integer pos4 = null;
                Integer pos5 = null;
                Integer pos6 = null;

                for (earthquake e : ae) {
                    if (e.getPubDate().equals(t)) {


                        Dearthquakes.add(e);


                        //M
                        Double Mag = Double.valueOf(e.getMag());
                        if (Mag > hm) {
                            hm = Mag;
                            pos = counter;
                        }

                        //D
                        if (Integer.valueOf(e.getDepth()) > hd) {
                            hd = Integer.valueOf(e.getDepth());
                            pos2 = counter;
                        }

                        //Lat
                        if (Double.valueOf(e.getgLat()) > cHLa) {
                            cHLa = Double.valueOf(e.getgLat());
                            pos3 = counter;
                        }
                        if (Double.valueOf(e.getgLat()) < cLLa) {
                            cLLa = Double.valueOf(e.getgLat());
                            pos4 = counter;
                        }


                        //Long
                        if (Double.valueOf(e.getgLong()) > cHLo) {
                            Log.e("entered", "1");
                            cHLo = Double.valueOf(e.getgLong());
                            pos5 = counter;
                        }
                        if (Double.valueOf(e.getgLong()) < cLLo) {
                            Log.e("entered", "2");
                            cLLo = Double.valueOf(e.getgLong());
                            pos6 = counter;
                        }

                        counter++;
                    }

                }
                if (Dearthquakes.isEmpty()) {
                    Toast toast = Toast.makeText(thiscontext, "There were no earthquakes at this time", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    for (earthquake e : Dearthquakes) {
                        Log.e("debug", e.getDescription());
                    }
                   displayResults(pos, pos2, pos3, pos4, pos5, pos6);
                }
            }
        };

    public void showDatePickerDialog(View v) {
        Dearthquakes.clear();
        earthquakesfinal.clear();
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
                Log.e("Error", "Date Error");
            }
        }
        DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth, mDay);
        dateDialog.show();

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            listApps.setAdapter(null);
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
        Double cHLa = 0.0;
        Double cLLa = 90.00;
        Double cHLo = -180.00;
        Double cLLo = 180.00;

        for(earthquake e: ae){
            if(e.getPubDate().equals(t)){
                //M

                //Adds earthquakes that match date inputed to a new array
                Dearthquakes.add(e);

                //M
                Double Mag = Double.valueOf(e.getMag());
                if (Mag > hm) {
                    hm = Mag;
                    pos = counter;
                }

                //D
                if (Integer.valueOf(e.getDepth()) > hd) {
                    hd = Integer.valueOf(e.getDepth());
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
        if (Dearthquakes.isEmpty()) {
            Toast toast = Toast.makeText(thiscontext, "There were no earthquakes on this date", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            for (earthquake e : Dearthquakes) {
                Log.e("debug", e.getDescription());
            }
            displayResults(pos, pos2, pos3, pos4, pos5, pos6);
        }
    }

    public void searchByName() {
        Dearthquakes.clear();
        earthquakesfinal.clear();
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        ae = dbh.returnall();
        Double hm = -1.0;
        Integer counter = 0;
        Integer hd = 0;
        Integer pos = null;
        Integer pos2 = null;
        Integer pos3 = null;
        Integer pos4 = null;
        Integer pos5 = null;
        Integer pos6 = null;
        Double cHLa = 0.0;
        Double cLLa = 90.00;
        Double cHLo = -180.00;
        Double cLLo = 180.00;


        String inputedname = name.getText().toString().toLowerCase();
        for (earthquake e : ae) {
            String T = e.getTitle().toLowerCase();
            if (T.contains(inputedname)) {
               Dearthquakes.add(e);
                Double Mag = Double.valueOf(e.getMag());
                if (Mag > hm) {
                    hm = Mag;
                    pos = counter;
                }

                //D
                if (Integer.valueOf(e.getDepth()) > hd) {
                    hd = Integer.valueOf(e.getDepth());
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


        if (Dearthquakes.isEmpty()) {
            Toast toast = Toast.makeText(thiscontext, "There were no earthquakes on this date", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            for (earthquake e : Dearthquakes) {
                Log.e("debug", e.getDescription());
            }
            displayResults(pos, pos2, pos3, pos4, pos5, pos6);
        }
        }








    public void displayResults(int p, int p1, int p2, int p3, int p4, int p5){
        //DISPLAY THIS!!!
        Log.e("HIGHESTMAG", Dearthquakes.get(p).getDescription());
        Log.e("HIGHESTDEPTh", Dearthquakes.get(p1).getDescription());
        Log.e("NORTH", Dearthquakes.get(p2).getDescription());
        Log.e("SOUTH", Dearthquakes.get(p3).getDescription());
        Log.e("WEST", Dearthquakes.get(p4).getDescription());
        Log.e("EAST", Dearthquakes.get(p5).getDescription());
        earthquakesfinal.add(new earthquake(Dearthquakes.get(p)));
        earthquakesfinal.add(new earthquake(Dearthquakes.get(p1)));
        earthquakesfinal.add(new earthquake(Dearthquakes.get(p2)));
        earthquakesfinal.add(new earthquake(Dearthquakes.get(p3)));
        earthquakesfinal.add(new earthquake(Dearthquakes.get(p4)));
        earthquakesfinal.add(new earthquake(Dearthquakes.get(p5)));
        earthquakesfinal.get(0).setAtr("Highest Mag");
        earthquakesfinal.get(1).setAtr("Lowest Depth");
        earthquakesfinal.get(2).setAtr("Most Northenly");
        earthquakesfinal.get(3).setAtr("Most Sothernly");
        earthquakesfinal.get(4).setAtr("Most Easterly");
        earthquakesfinal.get(5).setAtr("Most Westerly");

        for(earthquake e: earthquakesfinal){
            Log.e("h1", e.toString());
        }
        ArrayAdapter<earthquake> arrayAdapter = new ArrayAdapter<>(
                thiscontext, R.layout.list_item2, earthquakesfinal);
        listApps.setAdapter(arrayAdapter);

    }



}
