package guhh.com.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
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
import java.util.HashMap;
import java.util.List;

import entity.Daily_forecast;
import entity.HeWeather5;
import entity.Hourly_forecast;
import entity.Now;
import entity.WeatherEntity;
import lecho.lib.hellocharts.view.LineChartView;
import seivice.Util;

/**
 * Created by ggg on 2017/11/7.
 */

public class WeatherFragment extends Fragment {
    private Handler handler;
    private String city;
    private View view;

    private WeatherFragment(){}

    public WeatherFragment(String city,Handler handler){
        this.city = city;
        this.handler = handler;
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
            refreshWeather();
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
                Log.i("sssddd",response.body());
                try {
                    WeatherEntity weatherEntity = JSON.parseObject(response.body(),WeatherEntity.class);
                    if(weatherEntity == null){
                        Toast.makeText(getContext(),"获取天气失败！",Toast.LENGTH_SHORT).show();
                        myAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty,null));
                        return;
                    }
                    HeWeather5 heWeather5 = weatherEntity.getResult().getHeWeather5().get(0);
                    if(!"ok".equals(heWeather5.getStatus())){
                        Toast.makeText(getContext(),"获取天气失败！",Toast.LENGTH_SHORT).show();
                        myAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty,null));
                        return;
                    }
                    Log.i("sssddd1",heWeather5.getStatus());
                    dayWeathers.addAll(heWeather5.getDaily_forecast());
//                    Toast.makeText(getContext(),dayWeathers.size()+"-",Toast.LENGTH_SHORT).show();
                    myAdapter.notifyDataSetChanged();

                    String city = heWeather5.getBasic().getCity();
                    Message message = new Message();
                    message.obj = weatherEntity;
                    message.what = MainActivity.GET_WEATHER_DONE;
                    handler.sendMessage(message);

                    HashMap<String,WeatherEntity> h = new HashMap<String, WeatherEntity>();
                    UserData.weathers.put(city,weatherEntity);

                }catch (JSONException e){
                    myAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty,null));
                    Toast.makeText(getContext(),"解析数据出错！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                myAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty,null));
                Toast.makeText(getContext(),"网咯请求失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RecyclerView dayWeatherRv;
    private List<Daily_forecast> dayWeathers;
    private MyAdapter myAdapter;

    private void initView(View view){
        dayWeatherRv = (RecyclerView) view.findViewById(R.id.dayWeather_rv);
        dayWeathers = new ArrayList<>();
        myAdapter = new MyAdapter(R.layout.item_weather,dayWeathers);
        myAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        myAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.loading,null));
        myAdapter.isFirstOnly(false);
        dayWeatherRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        dayWeatherRv.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }

    class MyAdapter extends BaseQuickAdapter<Daily_forecast,BaseViewHolder> {

        public MyAdapter(int layout,@Nullable List<Daily_forecast> data) {
            super(layout,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Daily_forecast item) {
            String minTmp = item.getTmp().getMin();
            String maxTmp = item.getTmp().getMax();
            String wind = item.getWind().getDir()+"   "+item.getWind().getDeg()+"°    级别："+item.getWind().getSc()+"    "+item.getWind().getSpd()+"km/h";
            String hum = item.getHum();
            String dayWeather = item.getCond().getTxt_d();
            String dayCode = item.getCond().getCode_d();
            String nightWeather = item.getCond().getTxt_n();
            String nightCode = item.getCond().getCode_n();
            String sunStartTime = item.getAstro().getSr();
            String sunDownTime = item.getAstro().getSs();
            String date = String.valueOf(item.getDate());
            helper.setText(R.id.minTmp_tv,minTmp);
            helper.setText(R.id.maxTmp_tv,maxTmp);
            helper.setText(R.id.wind_tv,wind);
            helper.setText(R.id.hum_tv,hum);
            helper.setText(R.id.dayWeather_tv,dayWeather);
            helper.setText(R.id.nightWeather_tv,nightWeather);
            helper.setText(R.id.ss_tv,sunStartTime);
            helper.setText(R.id.sd_tv,sunDownTime);
            Util.setWeatherIcon((ImageView) helper.getView(R.id.dayIcon_iv),dayCode);
            Util.setWeatherIcon((ImageView) helper.getView(R.id.nightIcon_iv),nightCode);
            helper.setText(R.id.minTmp_tv,minTmp);
            helper.setText(R.id.date_tv,date);
            Log.i("sssddd",dayWeather+"---");
        }
    }

    public String getCity() {
        return city;
    }
}
