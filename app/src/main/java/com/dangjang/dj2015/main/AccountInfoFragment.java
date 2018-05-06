package com.dangjang.dj2015.main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.dialog.CustomDialogFragment;
import com.dangjang.dj2015.first.FirstActivity;
import com.dangjang.dj2015.first.LoginFragment;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.parentclass.FragmentCallbackIF;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Path;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends AppFragment implements View.OnClickListener{
    TextView email_textview;
    EditText name_edittext,phone_edittext;
    String pre_name,pre_phone;

    Handler mHandler = new Handler(Looper.getMainLooper());

    public final static int NW_USERINFO = 1;
    public final static int NW_USERINFO_CHANGE = 2;
    public final static int NW_USERLOGOUT = 3;
    public final static int NW_USERLEAVE = 4;
    public final static int NW_USERPWCHANGE =5;

    public AccountInfoFragment() {
        fragmentTitleStringResourceId = R.string.accountinfofragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_info, container, false);
        AppUtil.viewsSetOnClickListener(v,this,R.id.f_accountinfo_passwordchange_button,R.id.f_accountinfo_infochange_button,R.id.f_accountinfo_logout_button,R.id.f_accountinfo_leave_button);

        email_textview = (TextView)v.findViewById(R.id.f_accountinfo_email_textview);
        name_edittext = (EditText)v.findViewById(R.id.f_accountinfo_name_edittext);
        pre_name = name_edittext.getText().toString();
        phone_edittext = (EditText)v.findViewById(R.id.f_accountinfo_phone_edittext);
        pre_phone = phone_edittext.getText().toString();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkManager.getInstance().userInfo(NW_USERINFO,this,true,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_accountinfo_passwordchange_button:
                Intent intent = new Intent(getActivity(),PasswordChangeActivity.class);
                intent.putExtra("userid",email_textview.getText().toString());
                startActivityForResult(intent,0);
                break;
            case R.id.f_accountinfo_infochange_button:
                String name = name_edittext.getText().toString();
                String phone = phone_edittext.getText().toString();
                NetworkManager.getInstance().userModify(name,phone,NW_USERINFO_CHANGE,this);
                break;
            case R.id.f_accountinfo_logout_button:
            {
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.setContent("로그아웃 하시겠습니까?");
                customDialogFragment.setOnSubmitClickListener(new CustomDialogFragment.OnSubmitClickListener() {
                    @Override
                    public void onSubmit() {
                        NetworkManager.getInstance().userLogout(NW_USERLOGOUT, AccountInfoFragment.this);
                    }
                });
                customDialogFragment.setCancelVisible(true);
                customDialogFragment.show(getFragmentManager(), CustomDialogFragment.class.getName());
            }
                break;
            case R.id.f_accountinfo_leave_button:
            {
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.setContent("정말로 탈퇴 하시겠습니까?");
                customDialogFragment.setOnSubmitClickListener(new CustomDialogFragment.OnSubmitClickListener() {
                    @Override
                    public void onSubmit() {
                        NetworkManager.getInstance().userLeave(NW_USERLEAVE, AccountInfoFragment.this);
                    }
                });
                customDialogFragment.setCancelVisible(true);
                customDialogFragment.show(getFragmentManager(), CustomDialogFragment.class.getName());
            }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ResultCode.RESULT_OK:
                final String pre_pw = data.getStringExtra(PasswordChangeActivity.KEY_CURRENT_PASSWORD);
                final String new_pw = data.getStringExtra(PasswordChangeActivity.KEY_CHANGE_PASSWORD);
                NetworkManager.getInstance().userPwChange(pre_pw,new_pw,NW_USERPWCHANGE,this);
                break;
            case ResultCode.RESULT_FAIL:
                break;
            case ResultCode.RESULT_CANCEL:
                break;
        }
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){
            case NW_USERINFO:
                if(success && object != null) {
                    UserInfo userInfo = (UserInfo)object;
                    email_textview.setText(userInfo.user_id);
                    name_edittext.setText("");
                    name_edittext.getText().append(userInfo.name);
                    phone_edittext.setText("");
                    phone_edittext.getText().append(userInfo.phone);
                }
                break;
            case NW_USERINFO_CHANGE:
                if(success && object != null) {
                    Result result = (Result)object;
                    if(result.result == 1) {
                        showMessage("계정 정보 변경이 완료되었습니다.");
                    }else {
                        name_edittext.setText("");
                        name_edittext.getText().append(pre_name);
                        phone_edittext.setText("");
                        phone_edittext.getText().append(pre_phone);
                        networkErrorMsg("userinfo_change", result.toString(), "변경에 실패하였습니다.\n다시 한번 시도해주세요.");
                    }
                }
                break;
            case NW_USERLOGOUT:
            case NW_USERLEAVE:
                if(success && object != null) {
                    Result result = (Result)object;
                    if(result.result == 1) {
                        if(KEY == NW_USERLEAVE){
                            NetworkManager.getInstance().getConnection().userLogout().enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Response<Result> response, Retrofit retrofit) {
                                    Log.i("result",response.body().toString());
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getActivity(), FirstActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                @Override
                                public void onFailure(Throwable t) {
                                    Log.e("userlogout", t.toString());
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getActivity(), FirstActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                            return;
                        }
                        StorageManager storageManager = StorageManager.getInstance();
                        storageManager.setSettingValue("AUTOLOGIN", "F");
                        Intent intent = new Intent(getActivity(), FirstActivity.class);
                        intent.putExtra("LOGIN", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else
                        networkErrorMsg((KEY==NW_USERLOGOUT)?"userlogout":"userleave",result.toString());
                }
                break;
            case NW_USERPWCHANGE:
                if(success && object != null) {
                    Result result = (Result)object;
                    if(result.result == 1)
                        showMessage("비밀번호 변경이 완료되었습니다.");
                    else
                        networkErrorMsg("userpwchange",result.toString(),"변경에 실패하였습니다.\n다시 한번 시도해주세요.");
                }
                break;
            default:
                super.onResponse(KEY,success,object);
        }
    }
}
