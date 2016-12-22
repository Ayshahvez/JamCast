package net.JamCast.app.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.JamCast.app.weatherapp.data.Channel;
import net.JamCast.app.weatherapp.data.Condition;
import net.JamCast.app.weatherapp.data.DBAdapter;
import net.JamCast.app.weatherapp.data.Forecast;
import net.JamCast.app.weatherapp.data.Item;
import net.JamCast.app.weatherapp.data.LocationResult;
import net.JamCast.app.weatherapp.data.databaseActivity;
import net.JamCast.app.weatherapp.listener.GeocodingServiceListener;
import net.JamCast.app.weatherapp.listener.WeatherServiceListener;
import net.JamCast.app.weatherapp.service.WeatherCacheService;
import net.JamCast.app.weatherapp.service.GoogleMapsGeocodingService;
import net.JamCast.app.weatherapp.service.YahooWeatherService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceListener, GeocodingServiceListener, LocationListener {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private TextView currentDay;

   private TextView[] textView = new TextView[5];

  /*  private TextView textView0;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;*/
    private Button btn,notify;


    private YahooWeatherService weatherService;
    private GoogleMapsGeocodingService geocodingService;
    private WeatherCacheService cacheService;

    private ProgressDialog dialog;

    // weather service fail flag
    private boolean weatherServicesHasFailed = false;

    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        currentDay =  (TextView)findViewById(R.id.currentDay);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);

       textView[0] =  (TextView) findViewById(R.id.textView1);
        textView[1] =  (TextView) findViewById(R.id.textView2);
        textView[2] =  (TextView) findViewById(R.id.textView3);
        textView[3] =  (TextView) findViewById(R.id.textView4);
        textView[4] =  (TextView) findViewById(R.id.textView5);

        btn = (Button) findViewById(R.id.button);
        notify = (Button) findViewById(R.id.notify);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        weatherService = new YahooWeatherService(this);
        weatherService.setTemperatureUnit(preferences.getString(getString(R.string.pref_temperature_unit), null));

        geocodingService = new GoogleMapsGeocodingService(this);
        cacheService = new WeatherCacheService(this);

        if (preferences.getBoolean(getString(R.string.pref_needs_setup), true)) {
            startSettingsActivity();
        } else {

            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.show();

            String location = null;

            if (preferences.getBoolean(getString(R.string.pref_geolocation_enabled), true)) {
                String locationCache = preferences.getString(getString(R.string.pref_cached_location), null);

                if (locationCache == null) {
                    getWeatherFromCurrentLocation();
                } else {
                    location = locationCache;
                }
            } else {
                location = preferences.getString(getString(R.string.pref_manual_location), null);
            }

            if(location != null) {
                weatherService.refreshWeather(location);
            }
        }

    }


    public void viewEmp(View v){
        DBAdapter myDb = new DBAdapter(this);
        myDb.open();

        myDb.insertRow("John", "Cherry Garden", "Kingston", "Jamaica","4326447","manager","ve.z@hotmail.com");
        myDb.insertRow("Jack", "Kingston 6", "Kingston", "Jamaica","4040534","supervisor","ayshahvez@yahoo.com");
       // myDb.insertRow("ema", "Mona"," Mobay", "Jamaica","434324","Team Lead","ve.z");
        myDb.close();
        Toast.makeText(this,"Employee Data was loaded Successfully", Toast.LENGTH_LONG).show();
    }

    public void Notify(View v){

        DBAdapter myDb = new DBAdapter(this);
        myDb.open();

            switch(textList.get(0).toString()){
                case "Thunderstorms":
                    Intent rainIntent = new Intent(android.content.Intent.ACTION_SEND);
                    rainIntent .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    rainIntent .setType("plain/text");
                    rainIntent .setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    rainIntent .putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{myDb.databaseToString().toString()});
                    rainIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Employee Notification");
                    rainIntent .putExtra(android.content.Intent.EXTRA_TEXT, "The forecast is rainy today, You will not be required to work for 8 hours instead you are allowed up to 4 hours today");
                    startActivity( rainIntent );
                    break;

                case "Sunny":
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setType("plain/text");
                    emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{myDb.databaseToString().toString()});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Employee Notification");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "The forecast is Sunny today, You are required to come to work for regular 8 hours.");
                    startActivity(emailIntent);
                    break;

                default:
            }
     //   }
        Toast.makeText(this,"Employees were successfully notified of weather forecasts", Toast.LENGTH_LONG).show();


    }

public String getCurrentDay(){
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    String Cday=" ";

    switch (day) {
        case Calendar.SUNDAY:
            // Current day is Sunday
            Cday="Sunday";
            break;

        case Calendar.MONDAY:
            // Current day is Monday
            Cday="Monday";break;

        case Calendar.TUESDAY:
            Cday="Tuesday";break;

        case Calendar.WEDNESDAY:
            Cday="Wednesday";break;

        case Calendar.THURSDAY:
            Cday="Thursday";break;

        case Calendar.FRIDAY:
            Cday="Friday";break;

        case Calendar.SATURDAY:
            Cday="Saturday";break;
    }

    return Cday;
}


    public int randomInteger(int min, int max) {

        Random rand = new Random();

        // nextInt excludes the top value so we have to add 1 to include the top value
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public ArrayList getNext5Days(String day){

        ArrayList days = new ArrayList();
        ArrayList Next5days = new ArrayList();


        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");

     switch(getCurrentDay()){
         case "Sunday":

          /*   for(int x=0;x<days.size();x++)
             {
                 if(days.get(x)!="Sunday"){
                Next5days.add(x);
                 }
             }*/
             Next5days.add("Monday");
             Next5days.add("Tuesday");
             Next5days.add("Wednesday");
             Next5days.add("Thursday");
             Next5days.add("Friday");

             break;

         case "Monday":
             Next5days.add("Tuesday");
             Next5days.add("Wednesday");
             Next5days.add("Thursday");
             Next5days.add("Friday");
             Next5days.add("Saturday");

             break;

         case "Tuesday":
             Next5days.add("Wednesday");
             Next5days.add("Thursday");
             Next5days.add("Friday");
             Next5days.add("Saturday");
             Next5days.add("Sunday");

             break;

         case "Wednesday":
             Next5days.add("Thursday");
             Next5days.add("Friday");
             Next5days.add("Saturday");
             Next5days.add("Sunday");
             Next5days.add("Monday");

             break;

         case "Thursday":
             Next5days.add("Friday");
             Next5days.add("Saturday");
             Next5days.add("Sunday");
             Next5days.add("Monday");
             Next5days.add("Tuesday");

             break;

         case "Friday":
             Next5days.add("Saturday");
             Next5days.add("Sunday");
             Next5days.add("Monday");
             Next5days.add("Tuesday");
             Next5days.add("Wednesday");

             break;

         case "Saturday":

                     Next5days.add("Sunday");
                     Next5days.add("Monday");
                     Next5days.add("Tuesday");
                     Next5days.add("Wednesday");
                     Next5days.add("Thursday");

             break;
     }
        return Next5days;
    }

    private void getWeatherFromCurrentLocation() {
        // system's LocationManager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // medium accuracy for weather, good for 100 - 500 meters
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);

        String provider = locationManager.getBestProvider(locationCriteria, true);

        // single location update
        locationManager.requestSingleUpdate(provider, this, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.currentLocation:
                dialog.show();
                getWeatherFromCurrentLocation();
                return true;
            case R.id.settings:
                startSettingsActivity();
                return true;
            case R.id.employees:
                Intent intent2 = new Intent(getApplicationContext(),databaseActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   //////////////////////////////////

    ArrayList textList = new ArrayList();
    @Override
    public void serviceSuccess(Channel channel){
        dialog.hide();

        Condition condition = channel.getItem().getCondition();
    //   Forecast forecast = channel.getItem().getForecast();
        Item item = new Item();

        int resourceId = getResources().getIdentifier("drawable/icon_" + condition.getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);

        String temperatureLabel = getString(R.string.temperature_output, condition.getTemperature(), channel.getUnits().getTemperature());

        temperatureTextView.setText(temperatureLabel);
     conditionTextView.setText(condition.getDescription());
    //    conditionTextView.setText(channel.getItem().getForecast().getDescription());
        locationTextView.setText(channel.getLocation());

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        // currentDay.setText( new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));

        currentDay.setText(getCurrentDay());


       textList =  channel.getItem().getForecast().getTextList();
        ArrayList dateList =  channel.getItem().getForecast().getDatelist();
        ArrayList tempList =  channel.getItem().getForecast().getTemplist();


        ArrayList<String> dataa= getNext5Days(getCurrentDay());
        final String DEGREE  = "\u00b0";

        for (int x=0; x<5;x++) {


            textView[x].setText(dateList.get(x) + " " +
                    dataa.get(x) + " "+
                            textList.get(x)+" "+
                    tempList.get(x) + " " +
                    DEGREE

            );


        }


    }

    @Override
    public void serviceFailure(Exception exception) {
        // display error if this is the second failure
        if (weatherServicesHasFailed) {
            dialog.hide();
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            // error doing reverse geocoding, load weather data from cache
            weatherServicesHasFailed = true;
            // OPTIONAL: let the user know an error has occurred then fallback to the cached data
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();

            cacheService.load(this);
        }
    }

    @Override
    public void geocodeSuccess(LocationResult location) {
        // completed geocoding successfully
        weatherService.refreshWeather(location.getAddress());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.pref_cached_location), location.getAddress());
        editor.apply();
    }

    @Override
    public void geocodeFailure(Exception exception) {
        // GeoCoding failed, try loading weather data from the cache
        cacheService.load(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        geocodingService.refreshLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // OPTIONAL: implement your custom logic here
    }

    @Override
    public void onProviderEnabled(String s) {
        // OPTIONAL: implement your custom logic here
    }

    @Override
    public void onProviderDisabled(String s) {
        // OPTIONAL: implement your custom logic here
    }
}
