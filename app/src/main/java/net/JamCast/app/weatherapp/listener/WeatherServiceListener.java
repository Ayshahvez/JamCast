
package net.JamCast.app.weatherapp.listener;

import net.JamCast.app.weatherapp.data.Channel;

public interface WeatherServiceListener {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
