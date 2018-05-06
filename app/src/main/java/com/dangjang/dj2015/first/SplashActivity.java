package com.dangjang.dj2015.first;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.dialog.LoadingDialogFragment;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.main.MainActivity;

import java.util.Calendar;
import java.util.Date;

public class SplashActivity extends AppCompatActivity implements NetworkManager.NetworkInterface{
    Handler startHandler;
    Date change;
    LoadingDialogFragment loadingDialogFragment;

    public final static int NW_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int color;
            if(Build.VERSION.SDK_INT >= 23)
                color = getResources().getColor(R.color.colorPrimaryDark, getTheme());
            else
                color = getResources().getColor(R.color.colorPrimaryDark);
            window.setStatusBarColor(color);
        }
        loadingDialogFragment = new LoadingDialogFragment();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,1);
        change = calendar.getTime();

        startHandler = new Handler(Looper.getMainLooper());
        final String autologin = StorageManager.getInstance().getSettingValue("AUTOLOGIN");
        if(autologin != null && autologin.equals("T")){
            String id = StorageManager.getInstance().getSettingValue("ID");
            String pw = StorageManager.getInstance().getSettingValue("PW");
            NetworkManager.getInstance().userLogin(id, pw, NW_LOGIN, SplashActivity.this,false,false);
        }else{
            startHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //NetworkManager.getInstance().getTest();
                    Intent intent = new Intent(SplashActivity.this, FirstActivity.class);
                    if(autologin != null)
                        intent.putExtra("LOGIN",true);
                    startActivity(intent);
                    finish();
                }
            }, (System.currentTimeMillis() < change.getTime())?(change.getTime() - System.currentTimeMillis()):0);
        }
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {

        switch (KEY){
            case NW_LOGIN:
                if(success && object != null){
                    Result result = (Result)object;
                    if(result.result == 1) {
                        Log.i("login", result.toString());
                        startHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, (System.currentTimeMillis() < change.getTime())?(change.getTime() - System.currentTimeMillis()):0);
                    }else{
                        startHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, FirstActivity.class);
                                intent.putExtra("LOGIN", true);
                                startActivity(intent);
                                finish();
                            }
                        }, (System.currentTimeMillis() < change.getTime())?(change.getTime() - System.currentTimeMillis()):0);
                    }
                }else
                    Toast.makeText(this, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                break;
            default:
                Throwable t = (Throwable)object;
                Log.e("fail", "[KEY=" + KEY + " / detail=" + t.toString());
        }
    }

    @Override
    public void showLoadingDialog() {
        if(!loadingDialogFragment.isVisible() && !loadingDialogFragment.isAdded())
            loadingDialogFragment.show(getFragmentManager(), LoadingDialogFragment.class.getName());
    }

    @Override
    public void hideLoadingDialog() {
        loadingDialogFragment.dismiss();
    }

    public void networkErrorMsg(String log_name,String log_detail){
        networkErrorMsg(log_name, log_detail, "네트워크 오류입니다.\n다시 한번 시도해주세요.");
    }

    public void networkErrorMsg(String log_name,String log_detail,String snackbarMessage){
        Toast.makeText(this, snackbarMessage, Toast.LENGTH_SHORT).show();
        Log.e(log_name, log_detail);
    }
}
