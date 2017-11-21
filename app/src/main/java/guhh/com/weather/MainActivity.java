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
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
import seivice.Util;

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
                    String city = weatherEntity.getResult().getHeWeather5().get(0).getBasic().getCity();
                    if(fragments.get(viewPager.getCurrentItem()).getCity().equals(city)){
                        setDataInView(weatherEntity);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String city = fragments.get(position).getCity();
                WeatherEntity weatherEntity = UserData.weathers.get(city);
                if(weatherEntity!=null){
                    setDataInView(weatherEntity);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }




    @Override
    protected void onResume() {
        super.onResume();
        nowWeatherRl.post(new Runnable() {
            @Override
            public void run() {
                nowWeatherRl.getWidth(); // 获取宽度
                RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) nowBg_iv.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = nowWeatherRl.getHeight(); // 获取高度
                nowBg_iv.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                Log.i("sssddd",nowWeatherRl.getHeight()+"--");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
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



    private void setDataInView(WeatherEntity weatherEntity) {
        HeWeather5 heWeather5 = weatherEntity.getResult().getHeWeather5().get(0);
        //改变折线图
        List<Hourly_forecast>  hourly_forecasts = heWeather5.getHourly_forecast();
        lineChartHelper.changeLineChartDataValue(hourly_forecasts);

        //设置顶部面板数据

        Now now = heWeather5.getNow();
        String city = heWeather5.getBasic().getCity();
        String tmp = now.getTmp();
        String hum = now.getHum();
        String fl = now.getFl();
        String wind = now.getWind().getDir()+"    "+now.getWind().getDeg()+"°    级别："+now.getWind().getSc()+"    "+now.getWind().getSpd()+"km/h";
        String weather = now.getCond().getTxt();
        String weatherCode = now.getCond().getCode();
        String pm25 = heWeather5.getAqi().getCity().getPm25();
        hum_tv.setText(hum+"%");
        fl_tv.setText(fl+"℃");
        wind_tv.setText(wind);
        weather_tv.setText(weather);
        pm25_tv.setText(pm25);
        toolbar.setTitle(city);
        tmp_tv.setText(tmp);
        Util.setWeatherIcon(weather_iv,weatherCode);
        Util.setWeatherBg(nowBg_iv,weather);


//                    prepareDataAnimation(weatherEntity);
        lineChartView.startDataAnimation();
    }
    private Toolbar toolbar;
    private TextSwitcher weather_tv;
    private TextSwitcher wind_tv;
    private TextSwitcher tmp_tv;
    private TextSwitcher hum_tv;
    private TextSwitcher fl_tv;
    private TextSwitcher pm25_tv;
    private ImageSwitcher weather_iv;
    private LineChartView lineChartView;
    private RelativeLayout nowWeatherRl;
    private ImageSwitcher nowBg_iv;
    private void initView(){
        nowWeatherRl = (RelativeLayout) findViewById(R.id.nowWeather_rl);
        fragments = new ArrayList<>();
        fragments.add(new WeatherFragment("龙岗",handler));
        fragments.add(new WeatherFragment("盐田",handler));
        fragments.add(new WeatherFragment("北京",handler));
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


        weather_iv = (ImageSwitcher) findViewById(R.id.weather_iv);
        weather_iv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.imageview,null);
                return view;
            }
        });
        nowBg_iv = (ImageSwitcher) findViewById(R.id.nowBg_iv);
        nowBg_iv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = (ImageView) LayoutInflater.from(getBaseContext()).inflate(R.layout.imageview,null);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(layoutParams);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return view;
            }
        });
        weather_tv = (TextSwitcher) findViewById(R.id.weather_tv);
        weather_tv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView view = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.textview,null);
                view.setText("多云");
                view.setTextSize(18);
                return view;
            }
        });
        wind_tv = (TextSwitcher) findViewById(R.id.wind_tv);
        wind_tv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView view = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.textview,null);
                return view;
            }
        });
        tmp_tv = (TextSwitcher) findViewById(R.id.temperature_tv);
        tmp_tv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView view = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.textview,null);
                view.setText("0");
                view.setTextSize(60);
                return view;
            }
        });
        hum_tv = (TextSwitcher) findViewById(R.id.hum_tv);
        hum_tv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView view = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.textview,null);
                view.setText("0%");
                return view;
            }
        });
        fl_tv = (TextSwitcher) findViewById(R.id.fl_tv);
        fl_tv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView view = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.textview,null);
                view.setText("0℃");
                return view;
            }
        });
        pm25_tv = (TextSwitcher) findViewById(R.id.pm25_tv);
        pm25_tv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView view = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.textview,null);
                view.setText("0");
                return view;
            }
        });
        //折线图
        lineChartView = (LineChartView) findViewById(R.id.line_chart);

    }
}
