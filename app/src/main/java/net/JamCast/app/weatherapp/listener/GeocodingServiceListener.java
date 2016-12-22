
package net.JamCast.app.weatherapp.listener;

import net.JamCast.app.weatherapp.data.LocationResult;

public interface GeocodingServiceListener {
    void geocodeSuccess(LocationResult location);

    void geocodeFailure(Exception exception);
}
