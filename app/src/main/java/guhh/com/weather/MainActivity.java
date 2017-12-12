package guhh.com.weather;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.Gravity;
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
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;

import entity.HeWeather5;
import entity.Hourly_forecast;
import entity.Now;
import entity.WeatherEntity;
import lecho.lib.hellocharts.view.LineChartView;
import seivice.Util;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final int READ_PHONE_STATE_CODE = 100;
    private final int ACCESS_COARSE_LOCATION_CODE = 101;
    private final int ACCESS_FINE_LOCATION_CODE = 102;
    private final int READ_EXTERNAL_STORAGE_CODE = 103;
    private final int WRITE_EXTERNAL_STORAGE_CODE = 104;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

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
            int index = msg.getData().getInt("index");
            switch (what){
                case GET_WEATHER_DONE:
                    String city = weatherEntity.getResult().getHeWeather5().get(0).getBasic().getCity();
                    if(viewPager.getCurrentItem() == index){
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
//        setSupportActionBar(toolbar);

//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawer.addDrawerListener(toggle);
////
////
////        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        //begin------------------
        initView();
        checkPermission();
        getLocation();
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

    private void getLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求
    }

    private void checkPermission(){
        int temp = 0;
        String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION
        ,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            temp++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions,READ_PHONE_STATE_CODE);
            }
        }
        if(checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            temp++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions,ACCESS_COARSE_LOCATION_CODE);
            }
        }
        if(checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            temp++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions,ACCESS_FINE_LOCATION_CODE);
            }
        }
        if(checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            temp++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions,READ_EXTERNAL_STORAGE_CODE);
            }
        }
        if(checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            temp++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
            }
        }

        if(temp >= 0){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && mLocationClient !=null){
            mLocationClient.restart();
        }
        if(grantResults.length<=0)
            return;
        switch (requestCode){
            case READ_PHONE_STATE_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case READ_EXTERNAL_STORAGE_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case WRITE_EXTERNAL_STORAGE_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case ACCESS_COARSE_LOCATION_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case ACCESS_FINE_LOCATION_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        nowWeatherRl.post(new Runnable() {
//            @Override
//            public void run() {
//                nowWeatherRl.getWidth(); // 获取宽度
//                RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) nowBg_iv.getLayoutParams(); //取控件textView当前的布局参数
//                linearParams.height = nowWeatherRl.getHeight(); // 获取高度
//                nowBg_iv.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
//                mbView.setLayoutParams(linearParams);
//                Log.i("sssddd",nowWeatherRl.getHeight()+"--");
//            }
//        });

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
//        getSupportActionBar().setTitle(city);
        city_tv.setText(city);
//        toolbar.setTitle(city);
//        collapsingToolbarLayout.setTitle(city);
        tmp_tv.setText(tmp);
        Util.setWeatherIcon(weather_iv,weatherCode);
        Util.setWeatherBg(nowBg_iv,weather);


//                    prepareDataAnimation(weatherEntity);
        lineChartView.startDataAnimation();
    }

    //    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextSwitcher city_tv;
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
    private View mbView;
    private void initView(){
        mbView = findViewById(R.id.mb);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
//        collapsingToolbarLayout.setTitle("");
        nowWeatherRl = (RelativeLayout) findViewById(R.id.nowWeather_rl);

        fragments = new ArrayList<>();
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

        city_tv = (TextSwitcher) findViewById(R.id.city_tv);
        city_tv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView view = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.textview,null);
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
    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            Log.i("sssddd11",errorCode+"--");
            if(errorCode == 61 || errorCode == 161){
                fragments.add(new WeatherFragment(latitude,longitude,handler,fragments.size()));
                pagerAdapter.notifyDataSetChanged();
                mLocationClient.unRegisterLocationListener(this);
            }
        }
    }
}