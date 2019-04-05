package gcu.mpd.myapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private Button mBtn;
    private Button mTime;
    private Button nameb;
    private EditText name;
    private Button vmap3;
    private Button datetime;
    private ArrayList<earthquake> ae;
    ArrayList<earthquake> searched4equakes = new ArrayList<>();
    ArrayList<earthquake> earthquakesfinal = new ArrayList<>();
    private ListView listearthquakes;
    Context thiscontext;
    Double hm = -1.0;
    int ld = 1000;
    Integer hd = 0;
    Integer pos = null;
    Integer pos2 = null;
    Integer pos3 = null;
    Integer pos4 = null;
    Integer pos5 = null;
    Integer pos6 = null;
    Integer pos1 = null;
    Double cHLa = 0.0;
    Double cLLa = 90.00;
    Double cHLo = -180.00;
    Double cLLo = 180.00;
    int x = 0;
    String timeforspec = null;
    String dateforspec = null;

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
        vmap3 = (Button) v.findViewById(R.id.vmap3);
        listearthquakes = (ListView) v.findViewById(R.id.xmlistview2);
        datetime = (Button) v.findViewById(R.id.datetime);
        mBtn.setOnClickListener(this);
        mTime.setOnClickListener(this);
        nameb.setOnClickListener(this);
        vmap3.setOnClickListener(this);
        datetime.setOnClickListener(this);
        listearthquakes.setAdapter(null);



        //Pull database out
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        ae = dbh.returnall();
        String date = "";
        String time = "";


            //Normalise Time
            for (earthquake earthquake : ae) {
                String temp = earthquake.getPubDate();
                String M[] = temp.split(" ", 5);
                String T[] = M[4].split(":", 3);
                time = T[0] + T[1];
                //Normalise Date
                String M2[] = temp.split(" ", 5);
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(inputFormat.parse(M2[2]));
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MM");
                    String s = outputFormat.format(cal.getTime());
                    date = M2[1] + s + M2[3];
                } catch (java.text.ParseException e2) {
                    Log.e("Error", "Date Error");
                }
                String datetime = date + "/" + time;


                //Set one use var in earthquake class to normlised date time to not cause issues when redisplay the pub date later
                earthquake.setSearchdt(datetime);
            }
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPick:
                showDatePickerDialog();
                break;
            case R.id.time:
                showTimePickerDialog();
                break;
           case R.id.nameb:
                searchByName();
               break;
            case R.id.vmap3:
                sendDataToMap();
                break;
            case R.id.datetime:
                showDialog();
        }
    }

    public void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timeDialog= new TimePickerDialog(getActivity(), timePickerListener, hour, minute, false);
        timeDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            searched4equakes.clear();
            earthquakesfinal.clear();
            listearthquakes.setAdapter(null);
                String h = String.valueOf(hourOfDay);
                if (h.length() == 1) {
                    h = "0" + h;
                }
                String m = String.valueOf(minute);
                if (m.length() == 1) {
                    m = "0" + m;
                }
                String t = h + m;
                if(x == 1){
                   timeforspec = t;
                    datetimecalc();
                }else{
                    timecalc(t);
                }

            }


            public void timecalc(String t) {
                Double hm = -1.0;
                Integer counter = 0;
                for (earthquake e : ae) {
                    String T[] = e.getSearchdt().split("/", 2);
                    if (T[1].equals(t)) {
                        searched4equakes.add(e);
                        calculation(e, counter);
                        counter++;
                    }
                }
                if (searched4equakes.isEmpty()) {
                    Toast toast = Toast.makeText(thiscontext, "There were no earthquakes at this time", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                   displayResults(pos, pos2, pos3, pos4, pos5, pos6, pos1);
                }
            }
        };



    public void showDatePickerDialog() { ;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth, mDay);
        dateDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            searched4equakes.clear();
            earthquakesfinal.clear();
            listearthquakes.setAdapter(null);
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
            if(x == 1){
                dateforspec = t;
                showTimePickerDialog();
            } else {
                datecalc(t);
            }
        }
    };

    public void showDialog(){
        x = 1;
        showDatePickerDialog();
    }



    public void datecalc(String t){
        Double hm = -1.0;
        Integer counter = 0;
        for(earthquake e: ae){
            String T[] = e.getSearchdt().split("/", 2);
            if(T[0].equals(t)){
                searched4equakes.add(e);
                calculation(e, counter);
                counter++;
            }
        }
        if (searched4equakes.isEmpty()) {
            Toast toast = Toast.makeText(thiscontext, "There were no earthquakes on this date", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            displayResults(pos, pos2, pos3, pos4, pos5, pos6, pos1);
        }
    }

    public void searchByName() {
        searched4equakes.clear();
        earthquakesfinal.clear();
        listearthquakes.setAdapter(null);
        Integer counter = 0;

        String inputedname = name.getText().toString().toLowerCase();
        for (earthquake e : ae) {
            String T = e.getTitle().toLowerCase();
            if (T.contains(inputedname)) {
               searched4equakes.add(e);
               calculation(e, counter);
               counter++;
            }

            }
        if (searched4equakes.isEmpty()) {
            Toast toast = Toast.makeText(thiscontext, "There were no earthquakes with this name", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            for (earthquake e : searched4equakes) {
                Log.e("debug", e.getDescription());
            }
            displayResults(pos, pos2, pos3, pos4, pos5, pos6, pos1);
        }
        }

        public void calculation(earthquake e, int counter){
            //Mag
            Double Mag = Double.valueOf(e.getMag());
            if (Mag > hm) {
                hm = Mag;
                pos = counter;
            }
            //Depth
            if (Integer.valueOf(e.getDepth()) < ld) {
                ld = Integer.valueOf(e.getDepth());
                pos1 = counter;

            }
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
        }



    public void datetimecalc()
         {
            x = 0;
            String compare = dateforspec + "/" + timeforspec;
            for(earthquake e : ae){
              if(e.getSearchdt().equals(compare)){
                  earthquakesfinal.add(e);
                  earthquakesfinal.get(0).setAtr("Only earthquake");
                  SearchResultsAdapter sra = new SearchResultsAdapter(thiscontext, R.layout.search_results, earthquakesfinal);
                  listearthquakes.setAdapter(sra);
              }
            }
            if(earthquakesfinal.isEmpty()){
                Toast toast = Toast.makeText(thiscontext, "No earthquake found", Toast.LENGTH_SHORT);
                toast.show();
            }
         }




    public void displayResults(int p, int p1, int p2, int p3, int p4, int p5, int p6){
        //DISPLAY THIS!!!
        if(searched4equakes.size() == 1) {
            earthquakesfinal.add(new earthquake(searched4equakes.get(0)));
            earthquakesfinal.get(0).setAtr("Only earthquake");
            SearchResultsAdapter sra = new SearchResultsAdapter(thiscontext, R.layout.search_results, earthquakesfinal);
            listearthquakes.setAdapter(sra);
        }else{
            //Makes list of new earthquakes to display to the user
            //This list will contain mulitpule of the same earthquake
            earthquakesfinal.add(new earthquake(searched4equakes.get(p)));
            earthquakesfinal.add(new earthquake(searched4equakes.get(p1)));
            earthquakesfinal.add(new earthquake(searched4equakes.get(p6)));
            earthquakesfinal.add(new earthquake(searched4equakes.get(p2)));
            earthquakesfinal.add(new earthquake(searched4equakes.get(p3)));
            earthquakesfinal.add(new earthquake(searched4equakes.get(p4)));
            earthquakesfinal.add(new earthquake(searched4equakes.get(p5)));
            earthquakesfinal.get(0).setAtr("Highest Mag");
            earthquakesfinal.get(1).setAtr("Highest Depth");
            earthquakesfinal.get(2).setAtr("Lowest Depth");
            earthquakesfinal.get(3).setAtr("Most Northenly");
            earthquakesfinal.get(4).setAtr("Most Sothernly");
            earthquakesfinal.get(5).setAtr("Most Easterly");
            earthquakesfinal.get(6).setAtr("Most Westerly");

            for (earthquake e : earthquakesfinal) {
                Log.e("h1", e.toString());
            }

            SearchResultsAdapter sra = new SearchResultsAdapter(thiscontext, R.layout.search_results, earthquakesfinal);
            listearthquakes.setAdapter(sra);
        }
    }


    public void sendDataToMap(){
        if(earthquakesfinal.isEmpty()){
            Toast toast = Toast.makeText(thiscontext, "No search data to display on map", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            MapFragment3 map2 = new MapFragment3();
            Bundle bundle = new Bundle();
             bundle.putParcelableArrayList("aL", earthquakesfinal);
            map2.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, map2 )
                    .commit();
        }
    }
}
