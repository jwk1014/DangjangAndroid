package com.dangjang.dj2015.first;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.dialog.PasswordFindDialogFragment;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.main.MainActivity;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.productlist.AddressDisplayView;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends AppFragment implements View.OnClickListener{
    EditText id_edittext,pw_edittest;

    public final static int NW_LOGIN = 1;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        id_edittext = (EditText)v.findViewById(R.id.f_login_id_edittext);
        pw_edittest = (EditText)v.findViewById(R.id.f_login_pw_edittext);
        pw_edittest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login();
                return true;
            }
        });
        AppUtil.viewsSetOnClickListener(v, this, R.id.f_login_join_text, R.id.f_login_findpassword_textview, R.id.f_login_send_btn);//, R.id.in_bottom_notice_textview, R.id.in_bottom_support_textview);
//        LinearLayout layout = (LinearLayout)v.findViewById(R.id.f_login_bottom_layout);
//        AppUtil.viewsSetOnClickListener(layout, this, R.id.in_bottom_notice_textview, R.id.in_bottom_support_textview);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_login_send_btn:
                {
                    login();
                }
                break;
            case R.id.f_login_findpassword_textview:
                {
                    PasswordFindDialogFragment passwordFindDialogFragment = new PasswordFindDialogFragment();
                    passwordFindDialogFragment.show(getFragmentManager(),PasswordFindDialogFragment.class.getName());
                }
                break;
            case R.id.f_login_join_text:
                {
                    if(mActivityListener != null){
                        mActivityListener.doFragmentChange(new JoinFragment(),JoinFragment.class.getName(),R.anim.fade_in,R.anim.fade_out,true);
                    }
                }
                break;
            case R.id.in_bottom_notice_textview:
                TempActivity.startTempActivity(this,TempActivity.NOTICE);
                break;
            case R.id.in_bottom_support_textview:
                TempActivity.startTempActivity(this,TempActivity.SUPPORT);
                break;
        }
    }
    void login(){
        String id = id_edittext.getText().toString();
        String pw = pw_edittest.getText().toString();
//        if(id.length() == 0){
//            showMessage("아이디를 입력해주세요.");
//        }else if(pw.length() == 0){
//            showMessage("비밀번호를 입력해주세요.");
//        }else if(id.length() < 6 || !id.matches(AppUtil.REGEX_EMAIL) || pw.length() < 8 || !pw.matches(AppUtil.REGEX_PASSWORD)){
//            showMessage("아이디 또는 비밀번호를 확인해주세요.");
//        }else {
            NetworkManager.getInstance().userLogin(id, pw, NW_LOGIN, this);
//        }
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){

            case NW_LOGIN:
                if(success && object != null){
                    Result result = (Result)object;
                    if(result.result == 1) {
                        AppUtil.hideCurrentKeyboard(getActivity());
                        Log.i("login", result.toString());
                        StorageManager storageManager = StorageManager.getInstance();
                        storageManager.setSettingValue("AUTOLOGIN", "T");
                        String id = id_edittext.getText().toString();
                        String pw = pw_edittest.getText().toString();
                        storageManager.setSettingValue("ID", id);
                        storageManager.setSettingValue("PW", pw);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        showMessage("아이디 또는 비밀번호를 확인해주세요.");
                    }
                }else
                    showMessage("아이디 또는 비밀번호를 확인해주세요.");
                break;

            default:
                super.onResponse(KEY,false,object);

        }
    }
}
