
package net.JamCast.app.weatherapp.data;

import android.util.EventLogTags;

import net.JamCast.app.weatherapp.service.YahooWeatherService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by Ayshahvez on 10/3/2016.
 */


public class Forecast {
      private int temp;
    private String text="fix";
    private String date=null;

    public ArrayList<String> getTextList() {
        return Textlist;
    }

    public ArrayList<String> getTemplist() {
        return templist;
    }

    public ArrayList<String> getDatelist() {
        return datelist;
    }

    ArrayList<String>templist = new ArrayList<String>();
    ArrayList<String> datelist = new ArrayList<String>();
    ArrayList<String> Textlist = new ArrayList<String>();


    public int getTemp() {
        return temp;
    }


    public String getDate() {
        return date;
    }

    public String getDescription() {
        return text;
    }

    //@Override
    public void populate(JSONArray data) throws JSONException {

        try {
            for(int i=0;i<data.length();i++){
                JSONObject jDayForecast = data.getJSONObject(i);
                text =  jDayForecast.getString("text");
                Textlist.add(i,jDayForecast.getString("text"));
           //     temp = jDayForecast.getInt("low");
              templist.add(i,jDayForecast.getString("low"));
              //  date = jDayForecast.getString("date");
               datelist.add(i,jDayForecast.getString("date"));
            }


        } catch (Exception e) {

        }


    }

   // @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
          //  data.put("code", code);
            data.put("date",date);
            data.put("low", temp);
          //  data.put("low", Lowtemp);
            data.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }


}

