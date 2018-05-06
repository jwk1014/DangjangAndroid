package com.dangjang.dj2015.parentclass;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.dialog.LoadingDialogFragment;
import com.dangjang.dj2015.jsonclass.response.Result;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AppActivity extends AppCompatActivity implements FragmentCallbackIF, NetworkManager.NetworkInterface {
    public static final int TIME_BACK_TIMEOUT = 2000;

    private boolean autoBackPressed = true;
    private boolean isBackPressed = false;
    private boolean doingClose = false;
    protected View snackbar_container;
    protected int fragment_container;
    LoadingDialogFragment loadingDialogFragment;
    ActivityToFragmentIF activityToFragmentIF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public void onBackPressed() {
        if(activityToFragmentIF == null || !activityToFragmentIF.onBackKeyPressed()) {
            if (autoBackPressed) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else if (isBackPressed) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    if(!doingClose){
                        doingClose = true;
                        NetworkManager.getInstance().getConnection().userLogout().enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Response<Result> response, Retrofit retrofit) {
                                Log.i("result",response.body().toString());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                Log.e("userlogout",t.toString());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    isBackPressed = true;
                    snackbarShow(AppUtil.getXmlString(R.string.exit_message));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isBackPressed = false;
                        }
                    }, TIME_BACK_TIMEOUT);
                }
            } else
                super.onBackPressed();
        }
    }
    public void setAutoBackPressed(boolean autoBackPressed){
        this.autoBackPressed = autoBackPressed;
    }
    protected void setSnackBarContainer(int container){
        snackbar_container = findViewById(container);
    }

    protected void setFragmentContainer(int container){
        fragment_container = container;
    }

    @Override
    public void doFragmentChange(AppFragment f, String tag, int animation_in, int animation_out, boolean backstack_clear) {
        if (f != null) {
            FragmentManager fm = getFragmentManager();
            if(backstack_clear)
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            f.setOnFragmentCallbackIFListener(this);
            FragmentTransaction ft = fm.beginTransaction();
            //ft.setCustomAnimations(animation_in, animation_out);
            ft.replace(fragment_container, f,tag);
            if(!backstack_clear)
                ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void doDialogShow(AppDialogFragment f, String tag) {
        f.setOnFragmentCallbackIFListener(this);
        f.show(getFragmentManager(), tag);
    }

    @Override
    public void setActionBarTitle(String name) {
        setTitle(name);
    }

    @Override
    public void snackbarShow(String message) {
        Snackbar snackbar;
        if(loadingDialogFragment.isVisible()) {
            snackbar = Snackbar.make(loadingDialogFragment.getLayout(), message, Snackbar.LENGTH_SHORT);
        }else {
            AppUtil.hideCurrentKeyboard(this);
            snackbar = Snackbar.make(snackbar_container, message, Snackbar.LENGTH_SHORT);
        }
        View v = snackbar.getView();
        TextView textView = (TextView)v.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void showLoadingDialog() {
        if(!loadingDialogFragment.isVisible() && !loadingDialogFragment.isAdded())
            loadingDialogFragment.show(getFragmentManager(), LoadingDialogFragment.class.getName());
    }

    @Override
    public void hideLoadingDialog() {
        try {
            loadingDialogFragment.dismiss();
        }catch (Exception e){}
    }

    public void setOnBackKeyPressedListener(ActivityToFragmentIF activityToFragmentIF){
        this.activityToFragmentIF = activityToFragmentIF;
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        if(success){
           Log.i("success","[KEY="+KEY+" / object="+object);
        }else{
            Throwable t = (Throwable)object;
            Log.e("fail","[KEY="+KEY+" / detail="+t.toString());
        }
    }

    public void networkErrorMsg(String log_name,String log_detail){
        networkErrorMsg(log_name, log_detail, "네트워크 오류입니다.\n다시 한번 시도해주세요.");
    }

    public void networkErrorMsg(String log_name,String log_detail,String snackbarMessage){
        snackbarShow(snackbarMessage);
        Log.e(log_name, log_detail);
    }
}
