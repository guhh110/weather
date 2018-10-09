package guhh.com.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.transition.Visibility;
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
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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

import entity.Air;
import entity.Comf;
import entity.Cw;
import entity.Daily_forecast;
import entity.Drsg;
import entity.Flu;
import entity.HeWeather5;
import entity.Hourly_forecast;
import entity.Now;
import entity.RecycleViewDataEntity;
import entity.Sport;
import entity.Suggestion;
import entity.SuggestionEntity;
import entity.Trav;
import entity.Uv;
import entity.WeatherEntity;
import lecho.lib.hellocharts.view.LineChartView;
import seivice.Util;

/**
 * Created by ggg on 2017/11/7.
 */

public class WeatherFragment extends Fragment {
    private int index;
    private Handler handler;
    private String city;
    private View view;
    private List<Daily_forecast> dayWeathers;
    private List<SuggestionEntity> suggestions;
    private List<RecycleViewDataEntity> rlData;
    private String url;

    public WeatherFragment(){}

    @SuppressLint("ValidFragment")
    public WeatherFragment(String city, Handler handler, int index){
        this.index = index;
        this.city = city;
        this.handler = handler;
        url = "https://way.jd.com/he/freeweather?city="+city+"&appkey="+UserData.apkKey;
    }

    @SuppressLint("ValidFragment")
    public WeatherFragment(double latitude, double longitude, Handler handler, int index){
        this.index = index;
        this.handler = handler;
        url = "https://way.jd.com/he/freeweather?city="+latitude+","+longitude+"&appkey="+UserData.apkKey;
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
                    suggestions = getSuggestions(heWeather5.getSuggestion());

                    progressRlDataOfDayWeather(dayWeathers);
                    myAdapter.notifyDataSetChanged();

                    String city = heWeather5.getBasic().getCity();
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("index",index);
                    message.obj = weatherEntity;
                    message.setData(bundle);
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
                Toast.makeText(getContext(),"网络请求失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void progressRlDataOfDayWeather(List<Daily_forecast> dayWeathers) {
        rlData.clear();
        for (int i = 0;i<dayWeathers.size();i++) {
            Daily_forecast df = dayWeathers.get(i);
            RecycleViewDataEntity r = new RecycleViewDataEntity();
            r.setIndex(i);
            r.setObject(df);
            rlData.add(r);
        }
    }

    private void progressRlDataOfSuggestion(List<SuggestionEntity> suggestionEntities) {
        rlData.clear();
        for (int i = 0;i<suggestionEntities.size();i++) {
            SuggestionEntity sge = suggestionEntities.get(i);
            RecycleViewDataEntity r = new RecycleViewDataEntity();
            r.setIndex(i);
            r.setObject(sge);
            rlData.add(r);
        }
    }

    private List<SuggestionEntity> getSuggestions(Suggestion suggestion) {
        List<SuggestionEntity> suggestionEntities = new ArrayList<>();

        Air air = suggestion.getAir();
        Cw cw = suggestion.getCw();
        Drsg drag = suggestion.getDrsg();
        Flu flu = suggestion.getFlu();
        Sport sport = suggestion.getSport();
        Trav trav = suggestion.getTrav();
        Uv uv = suggestion.getUv();
        Comf comf = suggestion.getComf();

        SuggestionEntity s1 = new SuggestionEntity();
        SuggestionEntity s2 = new SuggestionEntity();
        SuggestionEntity s3 = new SuggestionEntity();
        SuggestionEntity s4 = new SuggestionEntity();
        SuggestionEntity s5 = new SuggestionEntity();
        SuggestionEntity s6 = new SuggestionEntity();
        SuggestionEntity s7 = new SuggestionEntity();
        SuggestionEntity s8 = new SuggestionEntity();
        s1.setName("air");
        s1.setBrf(air.getBrf());
        s1.setTxt(air.getTxt());
        s2.setName("cw");
        s2.setBrf(cw.getBrf());
        s2.setTxt(cw.getTxt());
        s3.setName("drsg");
        s3.setBrf(drag.getBrf());
        s3.setTxt(drag.getTxt());
        s4.setName("flu");
        s4.setBrf(flu.getBrf());
        s4.setTxt(flu.getTxt());
        s5.setName("sport");
        s5.setBrf(sport.getBrf());
        s5.setTxt(sport.getTxt());
        s6.setName("trav");
        s6.setBrf(trav.getBrf());
        s6.setTxt(trav.getTxt());
        s7.setName("comf");
        s7.setBrf(comf.getBrf());
        s7.setTxt(comf.getTxt());
        s8.setName("uv");
        s8.setBrf(uv.getBrf());
        s8.setTxt(uv.getTxt());


        suggestionEntities.add(s1);
        suggestionEntities.add(s2);
        suggestionEntities.add(s3);
        suggestionEntities.add(s4);
        suggestionEntities.add(s5);
        suggestionEntities.add(s6);
        suggestionEntities.add(s7);
        suggestionEntities.add(s8);

        return suggestionEntities;
    }

    private RecyclerView dayWeatherRv;
    private MyAdapter myAdapter;

    private void initView(View view){
        dayWeatherRv = (RecyclerView) view.findViewById(R.id.dayWeather_rv);
        dayWeathers = new ArrayList<>();
        rlData = new ArrayList<>();
        myAdapter = new MyAdapter(rlData);
        myAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        myAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.loading,null));
        myAdapter.isFirstOnly(false);
        dayWeatherRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        dayWeatherRv.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position == 0){
                    if(rlData.size()>0 && rlData.get(0).getItemType() == RecycleViewDataEntity.TYPE_DF){
                        myAdapter.notifyItemRangeRemoved(0,dayWeathers.size());
                        progressRlDataOfSuggestion(suggestions);
                        myAdapter.notifyDataSetChanged();
                    }else{
                        myAdapter.notifyItemRangeRemoved(0,dayWeathers.size());
                        progressRlDataOfDayWeather(dayWeathers);
                        myAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }

    class MyAdapter extends BaseMultiItemQuickAdapter<RecycleViewDataEntity,BaseViewHolder> {
        public MyAdapter(List<RecycleViewDataEntity> data) {
            super(data);
            addItemType(RecycleViewDataEntity.TYPE_DF,R.layout.item_weather);
            addItemType(RecycleViewDataEntity.TYPE_SG,R.layout.item_suggestion);
        }

//        @Override
//        protected void convert(BaseViewHolder helper, Object item) {
//            if(item instanceof Daily_forecast){
//                Daily_forecast daily_forecast = (Daily_forecast) item;
//                String minTmp = daily_forecast.getTmp().getMin();
//                String maxTmp = daily_forecast.getTmp().getMax();
//                String wind = daily_forecast.getWind().getDir()+"   "+daily_forecast.getWind().getDeg()+"°    级别："+daily_forecast.getWind().getSc()+"    "+daily_forecast.getWind().getSpd()+"km/h";
//                String hum = daily_forecast.getHum();
//                String dayWeather = daily_forecast.getCond().getTxt_d();
//                String dayCode = daily_forecast.getCond().getCode_d();
//                String nightWeather = daily_forecast.getCond().getTxt_n();
//                String nightCode = daily_forecast.getCond().getCode_n();
//                String sunStartTime = daily_forecast.getAstro().getSr();
//                String sunDownTime = daily_forecast.getAstro().getSs();
//                String date = String.valueOf(daily_forecast.getDate());
//                helper.setText(R.id.minTmp_tv,minTmp);
//                helper.setText(R.id.maxTmp_tv,maxTmp);
//                helper.setText(R.id.wind_tv,wind);
//                helper.setText(R.id.hum_tv,hum);
//                helper.setText(R.id.dayWeather_tv,dayWeather);
//                helper.setText(R.id.nightWeather_tv,nightWeather);
//                helper.setText(R.id.ss_tv,sunStartTime);
//                helper.setText(R.id.sd_tv,sunDownTime);
//                Util.setWeatherIcon((ImageView) helper.getView(R.id.dayIcon_iv),dayCode);
//                Util.setWeatherIcon((ImageView) helper.getView(R.id.nightIcon_iv),nightCode);
//                helper.setText(R.id.minTmp_tv,minTmp);
//                helper.setText(R.id.date_tv,date);
//            }
//
//        }

        @Override
        protected void convert(BaseViewHolder helper, RecycleViewDataEntity item) {
            switch (helper.getItemViewType()){
                case RecycleViewDataEntity.TYPE_DF:
                    setViewDataDayWeather(helper,item.getObject());
                    break;
                case RecycleViewDataEntity.TYPE_SG:
                    setViewDataSuggestion(helper,item.getObject());
                    break;

            }
            if(item.getIndex() == rlData.size()-1){
                Log.i("sssddd1",helper.getAdapterPosition()+"-"+helper.getLayoutPosition()+"-"+helper.getOldPosition()+"-");
                helper.setVisible(R.id.cv1,false);
                helper.setVisible(R.id.cv2,false);
            }else{
                helper.setVisible(R.id.cv1,true);
                helper.setVisible(R.id.cv2,true);
            }
        }

        private void setViewDataSuggestion(BaseViewHolder helper,Object item){
            SuggestionEntity suggestionEntity = (SuggestionEntity) item;
            String name = suggestionEntity.getName();
            String txt = suggestionEntity.getTxt();
            String brf = suggestionEntity.getBrf();
            helper.setText(R.id.brf_tv,brf);
            Util.setSuggestionName((TextView) helper.getView(R.id.name_tv), (ImageView) helper.getView(R.id.icon_iv),name);
            helper.setText(R.id.txt_tv,txt);

        }

        private void setViewDataDayWeather(BaseViewHolder helper,Object item){
            Daily_forecast daily_forecast = (Daily_forecast) item;
            String minTmp = daily_forecast.getTmp().getMin();
            String maxTmp = daily_forecast.getTmp().getMax();
            String wind = daily_forecast.getWind().getDir()+"   "+daily_forecast.getWind().getDeg()+"°    级别："+daily_forecast.getWind().getSc()+"    "+daily_forecast.getWind().getSpd()+"km/h";
            String hum = daily_forecast.getHum();
            String dayWeather = daily_forecast.getCond().getTxt_d();
            String dayCode = daily_forecast.getCond().getCode_d();
            String nightWeather = daily_forecast.getCond().getTxt_n();
            String nightCode = daily_forecast.getCond().getCode_n();
            String sunStartTime = daily_forecast.getAstro().getSr();
            String sunDownTime = daily_forecast.getAstro().getSs();
            String date = String.valueOf(daily_forecast.getDate());
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
//            Util.setWeatherBg((ImageView) helper.getView(R.id.itemBg_iv),dayWeather);
        }
    }

    public String getCity() {
        return city;
    }

    public int getIndex() {
        return index;
    }
}
