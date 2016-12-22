
package net.JamCast.app.weatherapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONPopulator {

    void populate(JSONObject data) throws JSONException;

    JSONObject toJSON();
}
