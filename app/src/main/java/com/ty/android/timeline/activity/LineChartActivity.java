package com.ty.android.timeline.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ty.android.timeline.R;
import com.ty.android.timeline.database.MyDBDao;
import com.ty.android.timeline.utils.TimeUtils;
import com.ty.android.timeline.utils.Tool;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import static com.ty.android.timeline.R.id.lineChart;
import static com.ty.android.timeline.database.MyDataBaseHelper.getInstance;
import static com.ty.android.timeline.utils.TimeUtils.getDate;
import static com.ty.android.timeline.utils.Tool.getDates;

/**
 * Created by Android on 2017/4/8.
 */

public class LineChartActivity extends AppCompatActivity implements View.OnClickListener {
    private LineChartView lineChart;

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private int day = 30;
    private List<String> dateList;
    private List<String> dateLableList = new ArrayList<>();
    private List<Integer> numList = new ArrayList<>();
    int type;

    private RelativeLayout r1, r2, r3, r4;
    private TextView tvAverage, tvHighest;
    private TextView t1, t2, t3, t4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        Log.d("lineoncreate", "onCreate: " + type);
        initView();
        initData();

        initLineChart();//初始化

        initEvent();
    }

    private void initEvent() {
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
    }

    private void initData() {
        if (numList.size() > 0) {

            numList.clear();
        }
        if (dateLableList.size() > 0) {
            dateLableList.clear();
        }
        dateList = getDates(30);
        for (int i = 0; i < dateList.size(); i++) {
            String res = TimeUtils.getDate(dateList.get(i));
            dateLableList.add(res.substring(5, res.length()));
        }
        if (type == 1) {
            for (int i = 0; i < dateList.size(); i++) {
                int res = MyDBDao.getInstance(LineChartActivity.this).readPinciDay(dateList.get(i));
                numList.add(res);
            }
        } else if (type == 2) {
            for (int i = 0; i < dateList.size(); i++) {
                int res = MyDBDao.getInstance(LineChartActivity.this).readGeShuDay(dateList.get(i));
                numList.add(res);
            }
        } else if (type == 3) {
            for (int i = 0; i < dateList.size(); i++) {
                int res = MyDBDao.getInstance(LineChartActivity.this).readAllOnADay(dateList.get(i));
                numList.add(res);
            }
        } else if (type == 4) {
            for (int i = 0; i < dateList.size(); i++) {
                int res = MyDBDao.getInstance(LineChartActivity.this).readAllTimeADay(dateList.get(i));
                res = res / 1000 / 60;
                numList.add(res);
            }
        }

        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();
    }

    private void initView() {
        lineChart = (LineChartView) findViewById(R.id.lineChart);
        r1 = (RelativeLayout) findViewById(R.id.r1);
        r2 = (RelativeLayout) findViewById(R.id.r2);
        r3 = (RelativeLayout) findViewById(R.id.r3);
        r4 = (RelativeLayout) findViewById(R.id.r4);

        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);

        tvAverage = (TextView) findViewById(R.id.tv_average);
        tvHighest = (TextView) findViewById(R.id.tv_highest);
    }

    private void updateView() {
        switch (getType()) {
            case 1:

                break;
        }
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#00CCCC"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setStrokeWidth(2);//线条的粗细，默认是3
//        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D4D4D4"));
        axisX.setLineColor(Color.parseColor("#D4D4D4"));

        axisX.setName("日期");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setLineColor(Color.parseColor("#D4D4D4"));
        axisY.setTextColor(Color.parseColor("#D4D4D4"));
        if (type == 2) {
            axisY.setName("个数");
        } else if (type == 4) {
            axisY.setName("分钟");
        } else {
            axisY.setName("次数");//y轴标注
        }
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        if (mAxisXValues.size() > 0) {
            mAxisXValues.clear();
        }
        for (int i = 0; i < dateLableList.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(dateLableList.get(i)));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        if (mPointValues.size() > 0) {
            mPointValues.clear();
        }
        for (int i = 0; i < numList.size(); i++) {
            mPointValues.add(new PointValue(i, numList.get(i)));
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.r1:
                setType(1);
                break;
            case R.id.r2:
                setType(2);
                break;
            case R.id.r3:
                setType(3);
                break;
            case R.id.r4:
                setType(4);
                break;

        }
        initData();
    }
}
