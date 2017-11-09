package guhh.com.weather;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.Hourly_forecast;
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
 * Created by guhh on 2017/11/9.
 */

public class LineChartHelper {
    private LineChartView lineChartView;
    private LineChartData lineChartData;
    private float max = -99;
    private float min = 99;

    private LineChartHelper(){};
    public LineChartHelper(LineChartView lineChartView){
        lineChartData = new LineChartData();
        initLineData();
        this.lineChartView = lineChartView;
        //设置行为属性，支持缩放、滑动以及平移
        this.lineChartView.setInteractive(true);
        this.lineChartView.setZoomType(ZoomType.HORIZONTAL);
        this.lineChartView.setMaxZoom((float) 2);//最大方法比例
        this.lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        this.lineChartView.setVisibility(View.VISIBLE);
        this.lineChartView.setLineChartData(lineChartData);
        this.lineChartView.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.bottom = -40;
        v.top = 100;
        this.lineChartView.setMaximumViewport(v);
        this.lineChartView.setCurrentViewport(v);

    }

    public  LineChartData getLineChartData(){
       return  lineChartData;
    }

    private void initLineData(){
        String[] strX = new String[]{"00:00","03:00","06:00","09:00","12:00","15:00","18:00","21:00"};
        int[] valueTemperaAll = new int[]{0,0,0,0,0,0,0,0};

//        Line line_TemperaAll = createLine(new int[]{},Color.parseColor("#FFCD41"));
        Line line_TemperaAll = createLine(valueTemperaAll, Color.parseColor("#FFCD41"));
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
    }

    public Axis createX(String[] labels){
        ArrayList mAxisValues = new ArrayList();
        for(int i = 0;i<labels.length;i++){
            mAxisValues.add(new AxisValue(i).setLabel(labels[i]));
        }

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(12);//设置字体大小
//        axisX.setMaxLabelChars(3); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setHasLines(true); //x 轴分割线

        return axisX;
    }

    public Axis createX(ArrayList<String> labels){
        ArrayList mAxisValues = new ArrayList();
        for(int i = 0;i<labels.size();i++){
            mAxisValues.add(new AxisValue(i).setLabel(labels.get(i)));
        }

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(12);//设置字体大小
//        axisX.setMaxLabelChars(3); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setHasLines(true); //x 轴分割线

        return axisX;
    }

    public Line createLine(int[] data, int color){

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

    public void changeLineChartDataValue(List<Hourly_forecast> hourly_forecasts) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        String nowDate = sdf.format(new Date());
        Line line = lineChartData.getLines().get(0);

        if(hourly_forecasts == null || line == null)
            return;
        ArrayList<String> xLabel = new ArrayList<>();
        List<PointValue> pointValuesTmpAll = line.getValues();
        int re = 0;
        for (int i = 0 ;i<hourly_forecasts.size();i++) {
            Hourly_forecast hourly_forecast = hourly_forecasts.get(i);
            String date = hourly_forecast.getDate();
            float tmp = Float.parseFloat(hourly_forecast.getTmp());
            if(!date.contains(nowDate) && re == 0){
                re =1;
                line.spIndex = i;
                Log.i("sssddd",i+"-----");
            }

            if(tmp>max)
                max = tmp;

            if(tmp<min)
                min = tmp;


            PointValue pointValue = pointValuesTmpAll.get(i);
            pointValue.setTarget(pointValue.getX(),tmp );
            xLabel.add(date.split(" ")[1]);
            Log.i("sssddd",date);
        }
        Log.i("sssddd",max+"-"+min);
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.bottom = min-2;
        v.top = max+2;
//        v.left = 0;
//        v.right = 9 - 1;
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);

        Axis axis = createX(xLabel);
        lineChartData.setAxisXBottom(axis);
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }
}
