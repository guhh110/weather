package guhh.com.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.HeWeather5;
import entity.Hourly_forecast;
import entity.Now;
import entity.WeatherEntity;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public final static int GET_WEATHER_DONE = 1;

    private ViewPager viewPager;
    private ArrayList<WeatherFragment> fragments;
    private PagerAdapter pagerAdapter;
    private LineChartHelper lineChartHelper ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            WeatherEntity weatherEntity = (WeatherEntity) msg.obj;
            switch (what){
                case GET_WEATHER_DONE:

                    setDataInView(weatherEntity);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //begin------------------
        initView();
        lineChartHelper = new LineChartHelper(lineChartView);

    }




    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private TextView weather_tv;
    private TextView wind_tv;
    private TextView tmp_tv;
    private TextView hum_tv;
    private TextView fl_tv;
    private TextView pm25_tv;
    private ImageView weather_iv;
    private LineChartView lineChartView;

    private void setDataInView(WeatherEntity weatherEntity) {
        HeWeather5 heWeather5 = weatherEntity.getResult().getHeWeather5().get(0);
        //改变折线图
        List<Hourly_forecast>  hourly_forecasts = heWeather5.getHourly_forecast();
        lineChartHelper.changeLineChartDataValue(hourly_forecasts);

        //设置顶部面板数据

        Now now = heWeather5.getNow();
        String tmp = now.getTmp();
        String hum = now.getHum();
        String fl = now.getFl();
        String wind = now.getWind().getDir()+"  "+now.getWind().getDeg()+"°  "+now.getWind().getSc()+"  "+now.getWind().getSpd()+"km/h";
        String weather = now.getCond().getTxt();
        String weatherCode = now.getCond().getCode();
        String pm25 = heWeather5.getAqi().getCity().getPm25();
        tmp_tv.setText(tmp);
        hum_tv.setText(hum);
        fl_tv.setText(fl);
        wind_tv.setText(wind);
        weather_tv.setText(weather);
        pm25_tv.setText(pm25);
        setWeatherIcon(weather_iv,weatherCode);


//                    prepareDataAnimation(weatherEntity);
        lineChartView.startDataAnimation();
    }

    private void initView(){
        fragments = new ArrayList<>();
        fragments.add(new WeatherFragment("龙岗",handler));
        fragments.add(new WeatherFragment("龙岗",handler));
        fragments.add(new WeatherFragment("龙岗",handler));
        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        viewPager = (ViewPager) findViewById(R.id.viewPage);
        viewPager.setAdapter(pagerAdapter);


        weather_iv = (ImageView) findViewById(R.id.weather_iv);
        weather_tv = (TextView) findViewById(R.id.weather_tv);
        wind_tv = (TextView) findViewById(R.id.wind_tv);
        tmp_tv = (TextView) findViewById(R.id.temperature_tv);
        hum_tv = (TextView) findViewById(R.id.hum_tv);
        fl_tv = (TextView) findViewById(R.id.fl_tv);
        pm25_tv = (TextView) findViewById(R.id.pm25_tv);
        //折线图
        lineChartView = (LineChartView) findViewById(R.id.line_chart);

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


}
