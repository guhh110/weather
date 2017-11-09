package guhh.com.weather;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import entity.HeWeather5;
import entity.Hourly_forecast;
import entity.Now;
import entity.WeatherEntity;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by ggg on 2017/11/7.
 */

public class WeatherFragment extends Fragment {
    private String city;
    private View view;
    private LineChartHelper lineChartHelper;

    private WeatherFragment(){}

    public WeatherFragment(String city){
        this.city = city;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment,null);
            initView(view);
            lineChartHelper = new LineChartHelper(lineChartView);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshWeather();
                }
            },2000);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("sssddd","onDestroyView");
    }

    private void refreshWeather(){
        String url = "https://way.jd.com/he/freeweather?city="+city+"&appkey="+UserData.apkKey;
//        String url = "http://192.168.15.188:8080/ImagePage/weather";
        OkGo.<String>get(url).tag(this).execute(new StringCallback(){

            @Override
            public void onSuccess(Response<String> response) {
                try {
                    WeatherEntity weatherEntity = JSON.parseObject(response.body(),WeatherEntity.class);
                    if(weatherEntity == null){
                        Toast.makeText(getContext(),"获取天气失败！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HeWeather5 heWeather5 = weatherEntity.getResult().getHeWeather5().get(0);
                    //改变折线图
                    List<Hourly_forecast>  hourly_forecasts = heWeather5.getHourly_forecast();
                    lineChartHelper.changeLineChartDataValue(hourly_forecasts);

                    //设置顶部面板数据
                    String city = heWeather5.getBasic().getCity();
                    Now now = heWeather5.getNow();
                    String tmp = now.getTmp();
                    String hum = now.getHum();
                    String fl = now.getFl();
                    String wind = now.getWind().getDir()+"  "+now.getWind().getDeg()+"°  "+now.getWind().getSc()+"  "+now.getWind().getSpd()+"km/h";
                    String weather = now.getCond().getTxt();
                    String weatherCode = now.getCond().getCode();
                    String pm25 = heWeather5.getAqi().getCity().getPm25();
                    location_tv.setText(city);
                    tmp_tv.setText(tmp);
                    hum_tv.setText(hum);
                    fl_tv.setText(fl);
                    wind_tv.setText(wind);
                    weather_tv.setText(weather);
                    pm25_tv.setText(pm25);
                    setWeatherIcon(weather_iv,weatherCode);


//                    prepareDataAnimation(weatherEntity);
                    lineChartView.startDataAnimation();
                    Log.i("sssddd",response.body());
                }catch (JSONException e){
                    Toast.makeText(getContext(),"解析数据出错！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(getContext(),"网咯请求失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWeatherIcon(ImageView weather_iv, String weatherCodeStr) {
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

    public boolean isDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour= sdf.format(new Date());
        int k  = Integer.parseInt(hour)  ;
        if ((k>=0 && k<6) ||(k >=18 && k<24)){
            return true;
        } else {
            return false;
        }
    }
    private RecyclerView dayWeatherRv;
    private LineChartView lineChartView;
    private ArrayList<WeatherEntity> dayWeathers;
    private MyAdapter myAdapter;
    private TextView location_tv;

    private TextView weather_tv;
    private TextView wind_tv;
    private TextView tmp_tv;
    private TextView hum_tv;
    private TextView fl_tv;
    private TextView pm25_tv;
    private ImageView weather_iv;

    private void initView(View view){
        weather_iv = (ImageView) view.findViewById(R.id.weather_iv);
        weather_tv = (TextView) view.findViewById(R.id.weather_tv);
        wind_tv = (TextView) view.findViewById(R.id.wind_tv);
        tmp_tv = (TextView) view.findViewById(R.id.temperature_tv);
        hum_tv = (TextView) view.findViewById(R.id.hum_tv);
        fl_tv = (TextView) view.findViewById(R.id.fl_tv);
        pm25_tv = (TextView) view.findViewById(R.id.pm25_tv);

        location_tv = (TextView) view.findViewById(R.id.location_tv);
        dayWeatherRv = (RecyclerView) view.findViewById(R.id.dayWeather_rv);
        dayWeathers = new ArrayList<>();
        dayWeathers.add(new WeatherEntity());
        dayWeathers.add(new WeatherEntity());
        dayWeathers.add(new WeatherEntity());

        myAdapter = new MyAdapter(R.layout.item_weather,dayWeathers);
        dayWeatherRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        dayWeatherRv.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        //折线图
        lineChartView = (LineChartView) view.findViewById(R.id.line_chart);

    }
    class MyAdapter extends BaseQuickAdapter<WeatherEntity,BaseViewHolder> {

        public MyAdapter(int layout,@Nullable List<WeatherEntity> data) {
            super(layout,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WeatherEntity item) {

        }
    }
}
