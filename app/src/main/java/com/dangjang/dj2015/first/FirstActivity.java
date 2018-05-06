package com.dangjang.dj2015.first;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.gcm.QuickstartPreferences;
import com.dangjang.dj2015.gcm.RegistrationIntentService;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FirstActivity extends AppActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setSnackBarContainer(R.id.a_first_main_layout);
        setFragmentContainer(R.id.activity_first_container);

        Intent intent = getIntent();
        AppFragment fragment;
        String autologin = StorageManager.getInstance().getSettingValue("AUTOLOGIN");
        if(intent != null && intent.getBooleanExtra("LOGIN",false))
            fragment = new LoginFragment();
        else if(autologin != null)
            fragment = new LoginFragment();
        else
            fragment = new AddressFragment();
            //fragment = new JoinFragment();

        fragment.setOnFragmentCallbackIFListener(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.activity_first_container,fragment);
        ft.commit();
    }
}
