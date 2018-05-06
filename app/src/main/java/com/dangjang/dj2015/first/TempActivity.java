package com.dangjang.dj2015.first;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dangjang.dj2015.R;
import com.dangjang.dj2015.cart.OrderHistoryFragment;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.main.AccountInfoFragment;
import com.dangjang.dj2015.main.NoticeFragment;
import com.dangjang.dj2015.support.SupportFragment;
import com.dangjang.dj2015.main.TermsFragment;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.parentclass.AppFragment;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class TempActivity extends AppActivity {
    public final static int TERMS = 1;
    public final static int NOTICE = 2;
    public final static int SUPPORT = 3;
    public final static int ACCOUNT = 4;
    public final static int ORDER_HISTORY = 5;
    public final static String KEY_FRAGMENT = "fragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        setSnackBarContainer(R.id.a_temp_main_layout);
        setFragmentContainer(R.id.a_temp_container);
        setAutoBackPressed(false);

        Intent intent = getIntent();
        int f_id = intent.getIntExtra(KEY_FRAGMENT,-1);
        if(f_id == -1){
            finish();
        }else{
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            AppFragment fragment = null;
            switch (f_id){
                case ACCOUNT:
                    fragment = new AccountInfoFragment();
                    break;
                case TERMS:
                    fragment = new TermsFragment();
                    break;
                case NOTICE:
                    fragment = new NoticeFragment();
                    break;
                case SUPPORT:
                    {
                        UserInfo userInfo = (UserInfo)intent.getSerializableExtra("userinfo");
                        if(userInfo != null)
                            fragment = SupportFragment.getInstance(userInfo);
                        else
                            fragment = new SupportFragment();
                    }
                    break;
                case ORDER_HISTORY:
                    fragment = new OrderHistoryFragment();
                    break;
            }
            if(fragment != null)
                fragment.setOnFragmentCallbackIFListener(this);
            ft.add(R.id.a_temp_container,fragment);
            ft.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static void startTempActivity(Activity activity,int f_id){
        Intent intent = new Intent(activity,TempActivity.class);
        intent.putExtra(KEY_FRAGMENT,f_id);
        activity.startActivity(intent);
    }

    public static void startTempActivityForSupport(Activity activity,UserInfo userInfo){
        Intent intent = new Intent(activity,TempActivity.class);
        intent.putExtra(KEY_FRAGMENT, SUPPORT);
        intent.putExtra("userinfo",userInfo);
        activity.startActivity(intent);
    }

    public static void startTempActivity(Fragment fragment,int f_id){
        startTempActivity(fragment.getActivity(),f_id);
    }
}
