package seivice;

import android.util.Log;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import entity.Hourly_forecast;
import entity.WeatherEntity;
import guhh.com.weather.R;

/**
 * Created by ggg on 2017/11/7.
 */


public class Util {
    public static void setWeatherIcon(ImageSwitcher weather_iv, String weatherCodeStr) {
        int weatherCodeInt = Integer.parseInt(weatherCodeStr);
        boolean isDay = isDay();
        Log.i("sssddd",isDay+"------");
        switch (weatherCodeInt){
            case 100:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.sunny);
                else
                    weather_iv.setImageResource(R.mipmap.sunny_night);
                break;
            case 101:
                weather_iv.setImageResource(R.mipmap.cloudy5);
                break;
            case 102:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.cloudy2);
                else
                    weather_iv.setImageResource(R.mipmap.cloudy2_night);
                break;
            case 103:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.cloudy1);
                else
                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
                break;
            case 104:
                weather_iv.setImageResource(R.mipmap.overcast);
                break;
//            case 200:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 201:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 202:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 203:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 204:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 205:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 206:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 207:
//
//                break;
//            case 208:
//
//                break;
//            case 209:
//
//                break;
//            case 210:
//
//                break;
//            case 211:
//
//                break;
//            case 212:
//
//                break;
//            case 213:
//
//                break;
            case 300:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.shower1);
                else
                    weather_iv.setImageResource(R.mipmap.shower1_night);
                break;
            case 301:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.shower2);
                else
                    weather_iv.setImageResource(R.mipmap.shower2_night);
                break;
            case 302:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.tstorm2);
                else
                    weather_iv.setImageResource(R.mipmap.tstorm2_night);
                break;
            case 303:
                weather_iv.setImageResource(R.mipmap.tstorm3);
                break;
            case 304:
                weather_iv.setImageResource(R.mipmap.hail);
                break;
            case 305:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 306:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 307:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 308:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 309:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 310:
                weather_iv.setImageResource(R.mipmap.shower3);
                break;
            case 311:
                weather_iv.setImageResource(R.mipmap.shower3);
                break;
            case 312:
                weather_iv.setImageResource(R.mipmap.shower3);
                break;
            case 313:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 400:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow1);
                else
                    weather_iv.setImageResource(R.mipmap.snow1_night);
                break;
            case 401:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow2);
                else
                    weather_iv.setImageResource(R.mipmap.snow2_night);
                break;
            case 402:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow3);
                else
                    weather_iv.setImageResource(R.mipmap.snow3_night);
                break;
            case 403:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow5);
                else
                    weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 404:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.sleet);
                break;
            case 405:
                weather_iv.setImageResource(R.mipmap.sleet);
                break;
            case 406:
                weather_iv.setImageResource(R.mipmap.sleet);
                break;
            case 407:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow1);
                else
                    weather_iv.setImageResource(R.mipmap.snow1_night);
                break;
            case 500:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.mist);
                else
                    weather_iv.setImageResource(R.mipmap.mist_night);
                break;
            case 501:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.fog);
                else
                    weather_iv.setImageResource(R.mipmap.fog_night);
                break;
            case 502:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.fog);
                else
                    weather_iv.setImageResource(R.mipmap.fog_night);
                break;
            case 503:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.fog);
                else
                    weather_iv.setImageResource(R.mipmap.fog_night);
                break;
            case 504:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.mist);
                else
                    weather_iv.setImageResource(R.mipmap.mist_night);
                break;
            default:
                weather_iv.setImageResource(0);
                break;
//            case 507:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 508:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 900:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 901:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 999:
        }
    }
    public static void setWeatherIcon(ImageView weather_iv, String weatherCodeStr) {
        int weatherCodeInt = Integer.parseInt(weatherCodeStr);
        boolean isDay = isDay();
        Log.i("sssddd",isDay+"------");
        switch (weatherCodeInt){
            case 100:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.sunny);
                else
                    weather_iv.setImageResource(R.mipmap.sunny_night);
                break;
            case 101:
                weather_iv.setImageResource(R.mipmap.cloudy5);
                break;
            case 102:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.cloudy2);
                else
                    weather_iv.setImageResource(R.mipmap.cloudy2_night);
                break;
            case 103:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.cloudy1);
                else
                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
                break;
            case 104:
                weather_iv.setImageResource(R.mipmap.overcast);
                break;
//            case 200:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 201:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 202:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 203:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 204:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 205:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 206:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.cloudy1);
//                else
//                    weather_iv.setImageResource(R.mipmap.cloudy1_night);
//                break;
//            case 207:
//
//                break;
//            case 208:
//
//                break;
//            case 209:
//
//                break;
//            case 210:
//
//                break;
//            case 211:
//
//                break;
//            case 212:
//
//                break;
//            case 213:
//
//                break;
            case 300:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.shower1);
                else
                    weather_iv.setImageResource(R.mipmap.shower1_night);
                break;
            case 301:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.shower2);
                else
                    weather_iv.setImageResource(R.mipmap.shower2_night);
                break;
            case 302:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.tstorm2);
                else
                    weather_iv.setImageResource(R.mipmap.tstorm2_night);
                break;
            case 303:
                weather_iv.setImageResource(R.mipmap.tstorm3);
                break;
            case 304:
                weather_iv.setImageResource(R.mipmap.hail);
                break;
            case 305:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 306:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 307:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 308:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 309:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 310:
                weather_iv.setImageResource(R.mipmap.shower3);
                break;
            case 311:
                weather_iv.setImageResource(R.mipmap.shower3);
                break;
            case 312:
                weather_iv.setImageResource(R.mipmap.shower3);
                break;
            case 313:
                weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 400:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow1);
                else
                    weather_iv.setImageResource(R.mipmap.snow1_night);
                break;
            case 401:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow2);
                else
                    weather_iv.setImageResource(R.mipmap.snow2_night);
                break;
            case 402:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow3);
                else
                    weather_iv.setImageResource(R.mipmap.snow3_night);
                break;
            case 403:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow5);
                else
                    weather_iv.setImageResource(R.mipmap.light_rain);
                break;
            case 404:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.sleet);
                break;
            case 405:
                weather_iv.setImageResource(R.mipmap.sleet);
                break;
            case 406:
                weather_iv.setImageResource(R.mipmap.sleet);
                break;
            case 407:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.snow1);
                else
                    weather_iv.setImageResource(R.mipmap.snow1_night);
                break;
            case 500:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.mist);
                else
                    weather_iv.setImageResource(R.mipmap.mist_night);
                break;
            case 501:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.fog);
                else
                    weather_iv.setImageResource(R.mipmap.fog_night);
                break;
            case 502:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.fog);
                else
                    weather_iv.setImageResource(R.mipmap.fog_night);
                break;
            case 503:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.fog);
                else
                    weather_iv.setImageResource(R.mipmap.fog_night);
                break;
            case 504:
                if(isDay)
                    weather_iv.setImageResource(R.mipmap.mist);
                else
                    weather_iv.setImageResource(R.mipmap.mist_night);
                break;
            default:
                weather_iv.setImageResource(0);
                break;
//            case 507:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 508:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 900:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 901:
//                if(isDay)
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                else
//                    weather_iv.setImageResource(R.mipmap.light_rain);
//                break;
//            case 999:
        }
    }
    public static boolean isDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour= sdf.format(new Date());
        int k  = Integer.parseInt(hour);
        if ((k>=0 && k<6) ||(k >=18 && k<24)){
            return false;
        } else {
            return true;
        }
    }

    public static void setWeatherBg(ImageSwitcher nowBg_iv, String weather) {
        if(nowBg_iv == null){
            Log.e("sssddd","nowBg_iv为空！");
            return;
        }

        int[] rainBgs = {R.drawable.rain_day,R.drawable.rain_day2,R.drawable.rain_day3,R.drawable.rain_day4,R.drawable.rain_night};
        int[] snowBgs = {R.drawable.snow,R.drawable.snow2,R.drawable.snow3,R.drawable.snow_day,R.drawable.snow_day2,R.drawable.snow_night,R.drawable.snow_night2};
        int[] qlBgs = {R.drawable.ql_night,R.drawable.ql_night2,R.drawable.sky_night_ql,R.drawable.sky};
        int[] ray = {R.drawable.flash,R.drawable.flash1,R.drawable.flash3,R.drawable.lightning};
        int[] wu = {R.drawable.fog,R.drawable.fog2,R.drawable.fog4,R.drawable.france};
        int[] mai = {R.drawable.fog3,R.drawable.fog5};
        int[] clouds = {R.drawable.clouds_night,R.drawable.dy,R.drawable.dy3,R.drawable.sky_day_dy};
        int[] yt = {R.drawable.sky_day_yt,R.drawable.yt};

        Random random = new Random();
        if(weather.contains("雨")){
            nowBg_iv.setImageResource(rainBgs[random.nextInt(rainBgs.length)]);
        }else if (weather.contains("晴")){
            nowBg_iv.setImageResource(qlBgs[random.nextInt(qlBgs.length)]);
        }else if(weather.contains("雪")){
            nowBg_iv.setImageResource(snowBgs[random.nextInt(snowBgs.length)]);
        }else if(weather.contains("雷")){
            nowBg_iv.setImageResource(ray[random.nextInt(ray.length)]);
        }else if(weather.contains("雾")){
            nowBg_iv.setImageResource(wu[random.nextInt(wu.length)]);
        }else if(weather.contains("霾")){
            nowBg_iv.setImageResource(mai[random.nextInt(mai.length)]);
        }else if(weather.contains("云")){
            nowBg_iv.setImageResource(clouds[random.nextInt(clouds.length)]);
        }else if(weather.contains("阴")){
            int index = random.nextInt(yt.length);
            Log.i("sssddd",nowBg_iv+"-"+yt+"-"+index);
            nowBg_iv.setImageResource(yt[index]);
        }
    }
}
