package gcu.mpd.myapp;

/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
 */

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
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *Class for the Search Fragment
 */
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
    Integer counter = 0;

    /**
     * Called when fragment first attached context from the parent when Fragment attaches
     * @param context context of the current state
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    /**
     * Called when the Fragment comes into the current view
     * sets onclick listener for all the buttons defined in the XML file.
     * Pulls the earthquake ArrayList from the database
     * Normalises the date and time for each earthquake object match the format that would be produced from data/time pickers
     * sets the normalised datetime to a new attribute within each earthquake object to not cause conflicts in display later..
     * @param inflater layout inflater
     * @param container viewgroup container
     * @param savedInstanceState bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Pull database out
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        ae = dbh.returnall();
        String date = "";
        String time = "";
        View v = inflater.inflate(R.layout.fragment_search,null);
        if(ae == null){
            Log.e("Empty", "Empty");
            Toast toast = Toast.makeText(thiscontext, "Could not get RSS Feed", Toast.LENGTH_SHORT);
            toast.show();
            return v;
        } else {


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
    }

    /**
     * Switch statement launches when one button is clicked, determines what button is clicked and fires the appropriate method
     * @param v view
     */
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

    /**
     * Shows a TimePicker allowing a user to select a time
     */
    public void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timeDialog= new TimePickerDialog(getActivity(), timePickerListener, hour, minute, false);
        timeDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        /**
         * Called once a user has selected a time. Clears the all past results if any.
         * Then sets the inputed time to the correct format and calls the calculation
         * @param timePicker timepicker
         * @param hourOfDay hour
         * @param minute miniute
         */
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

        /**
         * Takes in the time set by the user and finds out if any earthquake object took place at the time sent in
         * then either display that there was none at the time or sends the earthquakes found for further calculations
         * once all calculations have been preformed calls display results method
         * @param t time
         */
        public void timecalc(String t) {
                Double hm = -1.0;
                counter = 0;
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


    /**
     * Shows a date picker for the user
     */
    public void showDatePickerDialog() { ;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth, mDay);
        dateDialog.show();
    }

    /**
     * Called once the date has been set bu the user and sets the date chosen to the correct format and calls the calculation
     */
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            searched4equakes.clear();
            earthquakesfinal.clear();
            listearthquakes.setAdapter(null);
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

    /**
     * Used to display both date and time pickers
     */
    public void showDialog(){
        x = 1;
        showDatePickerDialog();
    }


    /**
     * Calculates if any earthquake objects took place on the date chosen by the user
     *  if none found display a message otherwise sends the earthquakes found for further calculations
     *  then displays calls displayresults
     * @param t date
     */
    public void datecalc(String t){
        Double hm = -1.0;
       counter = 0;
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

    /**
     * Used to find any earthquakes searched for by name
     * if non found displays a message, otherwise sends earthquakes on for further testing then calls display results
     */
    public void searchByName() {
        searched4equakes.clear();
        earthquakesfinal.clear();
        listearthquakes.setAdapter(null);
        counter = 0;

        String inputedname = name.getText().toString().toLowerCase();
        if (inputedname.isEmpty()) {
            Toast toast = Toast.makeText(thiscontext, "No text inputed", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            for (earthquake e : ae) {
                String T = e.getTitle().toLowerCase();
                Log.e("Title", e.getTitle().toLowerCase());
                Log.e("Vale", inputedname);
                if (T.contains(inputedname)) {
                    Log.e("object", e.toString());
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
    }

    /**
     * When called by either date, time, or name pickers calculates which earthquake from the list provied to the method
     * which earthquakee had highest Magnitute, Highest Depth, Lowest Depth, Most Easterly, Most Westly, Most Sothernly & Most Nornthly and adds
     * @param e earthquake object
     * @param counter counter
     */
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


    /**
     * Finds if any earthquakes took place on the data and time entered by the user
     * display message if non
     * otherwise sends earthquakes for further calculations then displays results
     */
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


    /**
     * From the results of the calculation determine which earthquake was equal to which value, e.g highest depth, so on.
     * adds each earthquake back into a new ArrayList so that if one earthquake has multiple values, it is added twice and can be displayed twice
     * then adds the value using set attribute to each earthquake and displays the result on the current view.
     * If only one earthquake is present, it displays only a singular earthquake.
     * Sets a custom adapter to populate the list view within the search results fragment
     * @param p  postion of earthquake for Highest mag
     * @param p1 postion of earthquake for Highest Depth
     * @param p2 postion of earthquake for Lowest Depth
     * @param p3 postion of earthquake for Most Northenly
     * @param p4 postion of earthquake for Most Sothernly
     * @param p5 postion of earthquake for Most Easterly
     * @param p6 postion of earthquake for Most Westerly
     */
    public void displayResults(int p, int p1, int p2, int p3, int p4, int p5, int p6){
        if(searched4equakes.size() == 1) {
            earthquakesfinal.add(new earthquake(searched4equakes.get(0)));
            earthquakesfinal.get(0).setAtr("Only earthquake");
            SearchResultsAdapter sra = new SearchResultsAdapter(thiscontext, R.layout.search_results, earthquakesfinal);
            listearthquakes.setAdapter(sra);
        }else{
            //Makes list of new earthquakes to display to the user
            //This list will contain multipul of the same earthquake

            Log.e("pos",  String.valueOf(p) +  String.valueOf(p1) + String.valueOf(p2)+ String.valueOf(p3) + String.valueOf(p4) +  String.valueOf(p5) +  String.valueOf(p6));
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
            SearchResultsAdapter sra = new SearchResultsAdapter(thiscontext, R.layout.search_results, earthquakesfinal);
            listearthquakes.setAdapter(sra);
            cHLa = 0.0;
            cLLa = 90.00;
            cHLo = -180.00;
            cLLo = 180.00;
            counter = 0;
            for(earthquake e : earthquakesfinal){
               Log.e("Search Results", e.getTitle() + e.getAtr());
            }
        }
    }

    /**
     * If the send to map button is clicked, sends the result earthquakes arraylist to map fragment 3 and sets map fragment 3 to the current fragment in display.
     */
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
