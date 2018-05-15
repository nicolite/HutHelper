package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.bean.Weather;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by nicolite on 17-10-13.
 */

public interface WeatherAPI {
    //http://wthrcdn.etouch.cn/weather_mini?city=株洲
    @GET("http://wthrcdn.etouch.cn/weather_mini?city=%E6%A0%AA%E6%B4%B2")
    Observable<Weather> getWeather();
}
