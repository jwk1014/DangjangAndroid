package com.dangjang.dj2015.main;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.cart.OrderHistoryFragment;
import com.dangjang.dj2015.first.TempActivity;
import com.dangjang.dj2015.gcm.OnGcmTokenCallback;
import com.dangjang.dj2015.gcm.QuickstartPreferences;
import com.dangjang.dj2015.gcm.RegistrationIntentService;
import com.dangjang.dj2015.jsonclass.response.ProductSearch;
import com.dangjang.dj2015.jsonclass.response.ProductVO;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.productdetail.ProductDetailActivity;
import com.dangjang.dj2015.productlist.OnProductRecentCallback;
import com.dangjang.dj2015.productlist.ProductListFragment;
import com.dangjang.dj2015.publicdata.MartItem;
import com.dangjang.dj2015.publicdata.MartProductItem;
import com.dangjang.dj2015.publicdata.ProductItem;
import com.dangjang.dj2015.support.SupportFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppActivity implements View.OnClickListener, OnGcmTokenCallback, OnProductRecentCallback{// implements NavigationView.OnNavigationItemSelectedListener {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    DrawerLayout drawer;
    boolean opened = false;
    View mainView;
    ProductRecentAdapter productRecentAdapter;
    TextView productRecentTextView;

    public final static int NW_PRODUCTRECENT = 1;
    public final static int NW_USERINFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registBroadcastReceiver();
        getInstanceIdToken();

        setSnackBarContainer(R.id.a_main_main_layout);
        setFragmentContainer(R.id.a_main_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainView = findViewById(R.id.a_main_main_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            /*@Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if(slideOffset == 0.0f){
                }else if(slideOffset == 1.0f){
                    AppUtil.hideCurrentKeyboard(MainActivity.this);
                }

                if(slideOffset > 0.5f && !opened){
                    setHomeAsUpIndicator(R.drawable.backward_w);
                    opened = true;
                }else if(slideOffset <= 0.5f && opened){
                    setHomeAsUpIndicator(R.drawable.threeline);
                    opened = false;
                }

                //mainView.setTranslationX(slideOffset * drawerView.getWidth());
            }*/

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setHomeAsUpIndicator(R.drawable.backward_w);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setHomeAsUpIndicator(R.drawable.threeline);
            }
        };
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.threeline);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View navView = findViewById(R.id.nav_view);
        AppUtil.viewsSetOnClickListener(
                navView,
                this,
                R.id.navHome,
                R.id.navHistory,
                R.id.navAccount,
                R.id.navNotice,
                R.id.navTerms,
                R.id.navSupport);
        productRecentTextView = (TextView)navView.findViewById(R.id.navProductRecentTextView);
        RecyclerView recyclerView = (RecyclerView)navView.findViewById(R.id.navProductRecentRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        productRecentAdapter = new ProductRecentAdapter(this);
        recyclerView.setAdapter(productRecentAdapter);
        productRecentAdapter.setOnItemClickListener(new ProductRecentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MartProductItem martProductItem) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("item", martProductItem);
                startActivity(intent);
            }
        });

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AppFragment fragment = new ProductListFragment();
        fragment.setOnFragmentCallbackIFListener(this);
        ft.add(R.id.a_main_container, fragment, ProductListFragment.class.getName());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navHome: {
                    AppFragment fragment = new ProductListFragment();
                    fragment.setOnFragmentCallbackIFListener(this);
                    doFragmentChange(fragment,ProductListFragment.class.getName(),R.anim.fade_in,R.anim.fade_out,true);
                    if(drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.navHistory:
                TempActivity.startTempActivity(this, TempActivity.ORDER_HISTORY);
                break;
            case R.id.navAccount:
                TempActivity.startTempActivity(this, TempActivity.ACCOUNT);
                break;
            case R.id.navNotice:
                TempActivity.startTempActivity(this, TempActivity.NOTICE);
                break;
            case R.id.navTerms:
                TempActivity.startTempActivity(this,TempActivity.TERMS);
                break;
            case R.id.navSupport:
                NetworkManager.getInstance().userInfo(NW_USERINFO,this,true,true);
                break;
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000).show();
            } else {
                Log.i("DJ GCM", "This device is not supported GCM.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void getInstanceIdToken(){
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }



    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    String token = intent.getStringExtra("token");
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void getToken() {
        getInstanceIdToken();
    }

    @Override
    public void onProductRecent() {
        NetworkManager.getInstance().productRecent(NW_PRODUCTRECENT,this);
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){
            case NW_PRODUCTRECENT:
                if(success && object != null){
                    List<ProductVO> productSearches = (List<ProductVO>)object;
                    productRecentAdapter.clear();
                    for(ProductVO productSearch:productSearches){
                        MartProductItem martProductItem = new MartProductItem();
                        MartItem martItem = new MartItem();
                        martItem.setId(productSearch.getMart_index());
                        martItem.setName(productSearch.getMart_name());
                        martProductItem.setMartItem(martItem);
                        ProductItem productItem = new ProductItem();
                        productItem.setId(productSearch.getIndex());
                        productItem.setName(productSearch.getName());
                        productItem.setUnit(productSearch.getUnit());
                        productItem.setImg_url(productSearch.getImg_url());
                        martProductItem.setProductItem(productItem);
                        martProductItem.setPrice(productSearch.getSale_price());
                        productRecentAdapter.add(martProductItem);
                    }
                    if(productRecentAdapter.getItemCount() > 0)
                        productRecentTextView.setVisibility(View.INVISIBLE);
                    else
                        productRecentTextView.setVisibility(View.VISIBLE);
                }
                break;
            case NW_USERINFO:
                if(success && object != null){
                    UserInfo userInfo = (UserInfo)object;
                    TempActivity.startTempActivityForSupport(this,userInfo);
                }
                break;
            default:
                super.onResponse(KEY, success, object);
        }
    }

    @Override
    public void networkErrorMsg(String log_name, String log_detail) {
        if(!log_name.equals("productRecent"))
            super.networkErrorMsg(log_name, log_detail);
    }
}
