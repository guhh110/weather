package guhh.com.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<PointValue> mPointerTempera = new ArrayList<>();
    private List<PointValue> mPointerHUM = new ArrayList<>();
    private List<AxisValue> mAxisValues = new ArrayList<>();
    private  LineChartData lineChartData;
    private Line line_Tempera;
    private Line line_HUM;

    //a5723d5a50ab4a5280691f0a3ea6f3d7
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
        initLineChartView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareDataAnimation();
                lineChartView.startDataAnimation();
            }
        },3000);

    }


    private void prepareDataAnimation() {
        Line line = lineChartData.getLines().get(0);
            for (PointValue value : line.getValues()) {
                // Here I modify target only for Y values but it is OK to modify X targets as well.
                value.setTarget(value.getX(), (float) Math.random() * 100);
            }
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

    private void initLineChartView(){
        String[] strX = new String[]{"01:00","04:00","07:00","10:00","13:00","16:00","19:00","21:00","24:00"};
        int[] valueYTempera = new int[]{13,31,56,43,98,65,32,74,69};
        int[] valueYHUM = new int[]{85,90,70,75,85,80,87,90,89,87};

        for(int i = 0;i<strX.length;i++){
            mAxisValues.add(new AxisValue(i).setLabel(strX[i]));
        }

        for(int i=0;i<valueYTempera.length;i++){
            mPointerTempera.add(new PointValue(i,valueYTempera[i]));
        }

        for(int i=0;i<valueYHUM.length;i++){
            mPointerHUM.add(new PointValue(i,valueYHUM[i]));
        }


        lineChartData = new LineChartData();

        line_Tempera = new Line(mPointerTempera).setColor(Color.parseColor("#FFCD41"));
        line_Tempera.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line_Tempera.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line_Tempera.setFilled(false);//是否填充曲线的面积
        line_Tempera.setHasLabels(false);//曲线的数据坐标是否加上备注
        line_Tempera.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line_Tempera.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line_Tempera.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line_Tempera.setStrokeWidth(2);

        line_HUM = new Line(mPointerHUM).setColor(Color.parseColor("#37aae4"));
        line_HUM.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line_HUM.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line_HUM.setFilled(false);//是否填充曲线的面积
        line_HUM.setHasLabels(false);//曲线的数据坐标是否加上备注
        line_HUM.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line_HUM.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line_HUM.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line_HUM.setStrokeWidth(2);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(12);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        lineChartData.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        List<Line> lines = new ArrayList<>();
        lines.add(line_Tempera);
        lines.add(line_HUM);

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

    }

    private LineChartView lineChartView;
    private void initView(){
        lineChartView = (LineChartView) findViewById(R.id.line_chart);

    }

}
