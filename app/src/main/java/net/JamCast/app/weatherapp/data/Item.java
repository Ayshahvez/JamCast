package net.JamCast.app.weatherapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Item implements JSONPopulator {
    private Condition condition;
   private Forecast forecast;

   public Forecast getForecast() {
        return forecast;
    }


    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) throws JSONException {

        condition = new Condition();
       forecast = new Forecast();

        condition.populate(data.optJSONObject("condition"));
      forecast.populate(data.optJSONArray("forecast"));
    }



    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("condition", condition.toJSON());
          data.put("forecast", forecast.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
