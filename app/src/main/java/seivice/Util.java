package seivice;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import entity.Hourly_forecast;
import entity.WeatherEntity;

/**
 * Created by ggg on 2017/11/7.
 */


public class Util {
    public static HashMap<String, ArrayList<String>> getLineData(WeatherEntity weatherEntity){
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        ArrayList<String> allTmp = new ArrayList<>();
        ArrayList<String> todayTmp = new ArrayList<>();
        ArrayList<String> xLabel = new ArrayList<>();

        String nowDate = sdf.format(new Date());
        HashMap<String, ArrayList<String>> lineData = new HashMap<>();
        List<Hourly_forecast> hourly_forecasts = weatherEntity.getResult().getHeWeather5().get(0).getHourly_forecast();
        for (int i = 0;i<hourly_forecasts.size();i+=3) {
            Hourly_forecast hourly_forecast = hourly_forecasts.get(i);
            String date = hourly_forecast.getDate();
            String tmp = hourly_forecast.getTmp();
            allTmp.add(tmp);
            xLabel.add(date.split(" ")[1]);
            if(date.contains(nowDate)){
                todayTmp.add(tmp);
            }
//            if(allTmp.size() == 8)
//                break;
        }
        Log.i("sssddd",allTmp.size()+"--"+todayTmp.size());
        lineData.put("allTmp",allTmp);
        lineData.put("todayTmp",todayTmp);
        lineData.put("xLabel",xLabel);

        return lineData;
    }
}
