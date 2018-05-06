package com.dangjang.dj2015.productdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.jsonclass.response.MartDetail;
import com.dangjang.dj2015.jsonclass.response.MartVO;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.main.MainActivity;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.skp.Tmap.TMapView;

public class MartDetailActivity extends AppActivity{
    TMapView mapView;
    LocationManager mLM;
    boolean isInitialized = false;
    boolean moveMylocation = false;
    TextView address_textview,phone_textview,time_textview,hollyday_textview,delivery_address_textview;

    public final static int NW_MARTDETAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mart_detail);
        setSnackBarContainer(R.id.a_martdetail_main_layout);

        mapView = (TMapView)findViewById(R.id.a_martdetail_mapView);
        mLM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("xxx마트");

        address_textview = (TextView)findViewById(R.id.a_martdetail_address_textview);
        phone_textview = (TextView)findViewById(R.id.a_martdetail_phone_textview);
        time_textview = (TextView)findViewById(R.id.a_martdetail_time_textview);
        hollyday_textview = (TextView)findViewById(R.id.a_martdetail_hollyday_textview);
        delivery_address_textview = (TextView)findViewById(R.id.a_martdetail_delivery_address_textview);

        ImageView imageView = (ImageView)findViewById(R.id.a_martdetail_mylocation_imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!moveMylocation) {
                    moveMylocation = true;
                    if (cacheLocation != null)
                        moveMap(cacheLocation.getLatitude(),cacheLocation.getLongitude());
                    try {
                        mLM.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mListener, null);
                    } catch (Exception e) {}
                }
            }
        });

        Intent intent = getIntent();
        int mart_id = intent.getIntExtra("mart_id",-1);
        if(mart_id != -1)
            NetworkManager.getInstance().martDetail(mart_id,NW_MARTDETAIL,this);
        new RegisterTask().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class RegisterTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            mapView.setSKPMapApiKey(AppUtil.getXmlString(R.string.tmap_api_key));
            mapView.setLanguage(TMapView.LANGUAGE_KOREAN);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isInitialized = true;
            moveMap(37.4869106, 126.93010479999998);
            setMyLocation(37.4869106, 126.93010479999998);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLM.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mListener, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mLM.removeUpdates(mListener);
        }catch (Exception e){}
    }

    private void moveMap(double lat, double lng) {
        mapView.setCenterPoint(lng, lat);
        mapView.setZoomLevel(17);
    }

    private void setMyLocation(double lat, double lng) {
        mapView.setLocationPoint(lng, lat);
        Bitmap bm = ((BitmapDrawable)getResources().getDrawable(R.drawable.locationmark_g)).getBitmap();
        mapView.setIcon(bm);
        mapView.setIconVisibility(true);
    }

    Location cacheLocation;
    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            cacheLocation = location;
            if(moveMylocation) {
                moveMap(location.getLatitude(), location.getLongitude());
                moveMylocation = false;
            }
            mLM.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){

            case NW_MARTDETAIL:
                if(success && object != null){
                    MartVO martDetail = (MartVO)object;
                    setTitle(martDetail.getName());
                    address_textview.setText(martDetail.getAddress_front()+" "+martDetail.getAddress_detail());
                    phone_textview.setText(martDetail.getPhone());
                    //time_textview.setText(martDetail.ad1 + " ~ " + martDetail.ad2);
                    time_textview.setText("10:00 ~ 23:00");
                    //hollyday_textview.setText(martDetail.hollyday);
                    hollyday_textview.setText("없음");
                    //delivery_address_textview.setText(martDetail.getAddressText());
                    delivery_address_textview.setText(martDetail.getAddress_front().substring(martDetail.getAddress_front().lastIndexOf(" ")+1)+" 일대");
                    moveMap(martDetail.getLat(), martDetail.getLng());
                    setMyLocation(martDetail.getLat(), martDetail.getLng());
                }
                break;

            default:
                super.onResponse(KEY,false,object);

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
