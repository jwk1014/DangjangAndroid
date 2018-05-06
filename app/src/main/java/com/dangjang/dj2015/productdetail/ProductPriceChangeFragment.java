package com.dangjang.dj2015.productdetail;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.dangjang.dj2015.publicdata.MartProductItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.MyApplication;
import com.dangjang.dj2015.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductPriceChangeFragment extends DialogFragment {
    private LineChart mChart;

    public ProductPriceChangeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.MyDialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_price_change, container, false);
        mChart = (LineChart)v.findViewById(R.id.chart1);
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        mChart.setPinchZoom(true);

        mChart.setBackgroundColor(Color.WHITE);

        mChart.setDescription(AppUtil.getXmlString(R.string.chart_description));

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("15/08/16");
        xVals.add("15/08/17");
        xVals.add("15/08/18");
        xVals.add("15/08/19");
        xVals.add("15/08/20");
        xVals.add("15/08/21");
        xVals.add("15/08/22");
        xVals.add("15/08/23");
        xVals.add("15/08/24");
        xVals.add("15/08/25");
        xVals.add("15/08/26");
        xVals.add("15/08/27");
        xVals.add("15/08/28");
        xVals.add("15/08/29");
        xVals.add("15/08/30");
        xVals.add("15/08/31");
        xVals.add("15/09/01");
        xVals.add("15/09/02");
        xVals.add("15/09/03");
        xVals.add("15/09/04");
        xVals.add("15/09/05");

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for(int i=0;i<20;i++)
            yVals1.add(new Entry(30000, i));
        yVals1.add(new Entry(7500, 20));

        LineDataSet set1 = new LineDataSet(yVals1, AppUtil.getXmlString(R.string.chart_data1_name));
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.RED);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(true);
        //        set1.setCircleHoleColor(Color.WHITE);

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        yVals2.add(new Entry(30000, 0));
        yVals2.add(new Entry(27000, 1));
        yVals2.add(new Entry(27000, 2));
        yVals2.add(new Entry(28000, 3));
        yVals2.add(new Entry(28000, 4));
        yVals2.add(new Entry(28000, 5));
        yVals2.add(new Entry(30000, 6));
        yVals2.add(new Entry(30000, 7));
        yVals2.add(new Entry(27000, 8));
        yVals2.add(new Entry(27000, 9));
        yVals2.add(new Entry(28000, 10));
        yVals2.add(new Entry(28000, 11));
        yVals2.add(new Entry(28000, 12));
        yVals2.add(new Entry(30000, 13));
        yVals2.add(new Entry(30000, 14));
        yVals2.add(new Entry(28000, 15));
        yVals2.add(new Entry(27000, 16));
        yVals2.add(new Entry(27000, 17));
        yVals2.add(new Entry(28000, 18));
        yVals2.add(new Entry(28000, 19));

        LineDataSet set2 = new LineDataSet(yVals2, AppUtil.getXmlString(R.string.chart_data2_name));
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.rgb(255, 153, 0));
        set2.setCircleColor(Color.rgb(255, 153, 0));
        set2.setLineWidth(2f);
        set2.setCircleSize(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.rgb(255, 153, 0));
        set2.setDrawCircleHole(true);
        set2.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set2);
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);
        //data.setValueTextColor(Color.WHITE);
        data.setValueTextColor(android.R.color.transparent);
        //data.setValueTextSize(9f);

        // set data
        mChart.setData(data);

        mChart.animateX(1000);

        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(ColorTemplate.getHoloBlue());
        rightAxis.setAxisMaxValue(35000f);
        rightAxis.setDrawGridLines(true);

        mChart.getAxisRight().setEnabled(false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int size[] = MyApplication.getDisplaySize();
        Dialog d = getDialog();
        d.getWindow().setLayout(size[0],size[1]);
        WindowManager.LayoutParams params = d.getWindow().getAttributes();
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        d.getWindow().setAttributes(params);
    }

}
