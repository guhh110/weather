package guhh.com.weather;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.Hourly_forecast;
import entity.WeatherEntity;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by ggg on 2017/11/7.
 */

public class WeatherFragment extends Fragment {
    private String city;

    private View view;
    private  LineChartData lineChartData;

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
            initLineChartView();

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
//        lineChartView.destroyDrawingCache();
//        lineChartData.finish();
//        lineChartData.getLines().clear();
    }

    private void initLineChartView(){
        String[] strX = new String[]{"00:00","03:00","06:00","09:00","12:00","15:00","18:00","21:00","24:00"};
        int[] valueTemperaAll = new int[]{0,0,0,0,0,0,0,0};

//        Line line_TemperaAll = createLine(new int[]{},Color.parseColor("#FFCD41"));
        Line line_TemperaAll = createLine(valueTemperaAll,Color.parseColor("#FFCD41"));
        line_TemperaAll.setHasPoints(true);
        Axis axisX = createX(strX);
        Axis axisY = new Axis();

        List<Line> lines = new ArrayList<>();
        lines.add(line_TemperaAll);

        lineChartData = new LineChartData();
        lineChartData.setAxisYLeft(axisY);  //Y轴设置在左边
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        lineChartData.setValueLabelBackgroundAuto(true);
        lineChartData.setLines(lines);

        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom((float) 2);//最大方法比例
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setVisibility(View.VISIBLE);
        lineChartView.startDataAnimation();
        lineChartView.setLineChartData(lineChartData);
        lineChartView.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.bottom = -40;
        v.top = 100;
//        v.left = 0;
//        v.right = 9 - 1;
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);

    }

    private Axis createX(String[] labels){
        ArrayList mAxisValues = new ArrayList();
        for(int i = 0;i<labels.length;i++){
            mAxisValues.add(new AxisValue(i).setLabel(labels[i]));
        }

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(12);//设置字体大小
//        axisX.setMaxLabelChars(3); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setHasLines(true); //x 轴分割线

        return axisX;
    }

    private Axis createX(ArrayList<String> labels){
        ArrayList mAxisValues = new ArrayList();
        for(int i = 0;i<labels.size();i++){
            mAxisValues.add(new AxisValue(i).setLabel(labels.get(i)));
        }

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(12);//设置字体大小
//        axisX.setMaxLabelChars(3); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setHasLines(true); //x 轴分割线

        return axisX;
    }

    private Line createLine(int[] data,int color){

        ArrayList<PointValue> pointValues = new ArrayList<>();
        for(int i=0;i<data.length;i++){
            pointValues.add(new PointValue(i,data[i]));
        }
        Line line = new Line(pointValues).setColor(color);
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setStrokeWidth(2);
        return line;
    }


    private RecyclerView dayWeatherRv;
        private LineChartView lineChartView;
    private ArrayList<WeatherEntity> dayWeathers;
    private MyAdapter myAdapter;
    private void initView(View view){
        lineChartView = (LineChartView) view.findViewById(R.id.line_chart);
        dayWeatherRv = (RecyclerView) view.findViewById(R.id.dayWeather_rv);
        dayWeathers = new ArrayList<>();
        dayWeathers.add(new WeatherEntity());
        dayWeathers.add(new WeatherEntity());
        dayWeathers.add(new WeatherEntity());

        myAdapter = new MyAdapter(R.layout.item_weather,dayWeathers);
        dayWeatherRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        dayWeatherRv.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    private void prepareDataAnimation(WeatherEntity weatherEntity) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        String nowDate = sdf.format(new Date());

        if(lineChartData == null)
            return;
        Line lineTmpAll = lineChartData.getLines().get(0);
        ArrayList<String> xLabel = new ArrayList<>();
        List<PointValue> pointValuesTmpAll = lineTmpAll.getValues();
        List<Hourly_forecast> hourly_forecasts = weatherEntity.getResult().getHeWeather5().get(0).getHourly_forecast();
        int re = 0;
        int index = 0;
        float maxTmp = -99;
        float minTmp = 99;
        for (int i = 0 ;i<hourly_forecasts.size() && index < 8;i+=3) {
            Hourly_forecast hourly_forecast = hourly_forecasts.get(i);
            String date = hourly_forecast.getDate();
            float tmp = Float.parseFloat(hourly_forecast.getTmp());
            if(!date.contains(nowDate) && re == 0){
                re =1;
                lineTmpAll.spIndex = index;
            }

            if(tmp>maxTmp)
                maxTmp = tmp;

            if(tmp<minTmp)
                minTmp = tmp;


            PointValue pointValue = pointValuesTmpAll.get(index);
            pointValue.setTarget(pointValue.getX(),tmp );
            xLabel.add(index,date.split(" ")[1]);
            index++;
        }
        Log.i("sssddd",maxTmp+"-"+minTmp);
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.bottom = minTmp-5;
        v.top = maxTmp+5;
//        v.left = 0;
//        v.right = 9 - 1;
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);

        Axis axis = createX(xLabel);
        lineChartData.setAxisXBottom(axis);
    }

    private void refreshWeather(){
//        String url = "https://way.jd.com/he/full_weather?city=beijing&lang=en&appkey="+UserData.apkKey;
        String url = "http://192.168.15.188:8080/ImagePage/weather";
        OkGo.<String>get(url).tag(this).execute(new StringCallback(){

            @Override
            public void onSuccess(Response<String> response) {
                //lineChartData.setAxisXBottom(createX(new String[]{"明天 01时","，明天 00:00"}));
                WeatherEntity weatherEntity = JSON.parseObject(response.body(),WeatherEntity.class);
//                HashMap<String,ArrayList<String>> data = Util.getLineData(weatherEntity);
                prepareDataAnimation(weatherEntity);
                lineChartView.startDataAnimation();
                Log.i("sssddd",response.body());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(getContext(),"网咯请求失败！",Toast.LENGTH_SHORT).show();
            }
        });
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
