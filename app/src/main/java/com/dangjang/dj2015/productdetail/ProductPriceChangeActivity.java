package com.dangjang.dj2015.productdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.MartProductItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ProductPriceChangeActivity extends AppCompatActivity{
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_price_change);

        setTitle("가격 정보 확인");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int colorPrimary = AppUtil.getResourceColor(R.color.colorPrimary);
        int colorPrimaryDark = AppUtil.getResourceColor(R.color.colorPrimaryDark);

        mChart = (LineChart)findViewById(R.id.chart1);
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-20);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
        for(int i=0;i<21;i++) {
            xVals.add(simpleDateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE,1);
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        float maxVal = 0;
        if(getIntent() != null && getIntent().getSerializableExtra("martProductItem") != null) {
            MartProductItem martProductItem = (MartProductItem)getIntent().getSerializableExtra("martProductItem");
            TextView martname = (TextView) findViewById(R.id.a_productpricechange_martname_textview);
            martname.setText(martProductItem.getMartItem().getName());
            TextView productname = (TextView) findViewById(R.id.a_productpricechange_productname_textview);
            productname.setText(martProductItem.getProductItem().getName());
            for(int i=0;i<21;i++)
                yVals1.add(new Entry(martProductItem.getPrice(), i));
            TextView current_textview = (TextView)findViewById(R.id.a_productpricechange_current_cost_textview);
            current_textview.setText(AppUtil.getMoneyStr(martProductItem.getPrice())+"원");
            TextView avg_textview = (TextView)findViewById(R.id.a_productpricechange_avg_cost_textview);
            avg_textview.setText(AppUtil.getMoneyStr(martProductItem.getPrice())+"원");
            TextView min_textview = (TextView)findViewById(R.id.a_productpricechange_min_cost_textview);
            min_textview.setText(AppUtil.getMoneyStr(martProductItem.getPrice())+"원");
            TextView max_texTextView = (TextView)findViewById(R.id.a_productpricechange_max_cost_textview);
            max_texTextView.setText(AppUtil.getMoneyStr(martProductItem.getPrice())+"원");
            for(maxVal=5000;martProductItem.getPrice()*2 > maxVal;maxVal+=5000);
        }

        LineDataSet set1 = new LineDataSet(yVals1, AppUtil.getXmlString(R.string.chart_data1_name));
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(colorPrimary);
        set1.setCircleColor(colorPrimary);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(colorPrimary);
        set1.setHighLightColor(colorPrimaryDark);
        set1.setDrawCircleHole(true);
        //        set1.setCircleHoleColor(Color.WHITE);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);
        //data.setValueTextColor(Color.BLACK);
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
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaxValue(maxVal);
        leftAxis.setDrawGridLines(true);
        mChart.getAxisRight().setEnabled(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
