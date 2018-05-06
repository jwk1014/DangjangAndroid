package com.dangjang.dj2015.first;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.dialog.AuthorizationDialogFragment;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.Sms;
import com.dangjang.dj2015.main.AddressSearchActivity;
import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.PhoneManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.main.MainActivity;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.productdetail.CustomCheckBox;
import com.dangjang.dj2015.publicdata.AddressInfo;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class JoinFragment extends AppFragment implements View.OnClickListener, AuthorizationCallbackIF,CustomCheckBox.OnCheckedListener{
    EditText address_edittext,email_edittext,phone_edittext,password_edittext,password_confirm_edittext;
    CustomCheckBox2 terms_checkbox;
    CustomCheckBox authorization_button;
    private boolean phone_authorization;

    public final static int NW_JOIN = 1;
    public final static int NW_LOGIN = 2;
    public final static int NW_AUTORIZATION = 3;

    public final static String ARG_ADDRESS = "address";
    private AddressInfo addressInfo;

    public JoinFragment() {
    }

    public static JoinFragment newInstance(AddressInfo addressInfo){
        JoinFragment joinFragment = new JoinFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ADDRESS, addressInfo);
        joinFragment.setArguments(args);
        return joinFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            addressInfo = (AddressInfo)getArguments().getSerializable(ARG_ADDRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_join, container, false);

        terms_checkbox = (CustomCheckBox2)v.findViewById(R.id.f_join_terms_checkbox);
        terms_checkbox.addOnCheckedListener(new CustomCheckBox2.OnCheckedListener() {
            @Override
            public void onChecked(CustomCheckBox2 view, boolean before) {
                if(!before){
                    TempActivity.startTempActivity(getActivity(),TempActivity.TERMS);
                }
            }
        });
        address_edittext = (EditText)v.findViewById(R.id.f_join_address_edittext);
        email_edittext = (EditText)v.findViewById(R.id.f_join_email_edittext);
        phone_edittext = (EditText)v.findViewById(R.id.f_join_phonenumber_edittext);
        password_edittext = (EditText)v.findViewById(R.id.f_join_password_edittext);
        password_confirm_edittext = (EditText)v.findViewById(R.id.f_join_password_confirm_edittext);
        password_confirm_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                join();
                return true;
            }
        });

        AppUtil.viewsSetOnClickListener(v, this,R.id.f_join_address_edittext_textview,R.id.f_join_send_btn, R.id.f_join_login_text, R.id.f_join_terms_textview, R.id.in_bottom_notice_textview, R.id.in_bottom_support_textview);

        authorization_button = (CustomCheckBox)v.findViewById(R.id.f_join_authorization_checkbox);
        authorization_button.addOnCheckedListener(this);

        String phoneNumber = PhoneManager.getInstance().getPhoneNumber();
        if(phoneNumber != null)
            phone_edittext.setText(phoneNumber.replaceAll("-",""));

        if(addressInfo != null)
            address_edittext.setText(addressInfo.getAddressString());

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ResultCode.RESULT_OK){
            addressInfo = (AddressInfo)data.getSerializableExtra(AddressSearchActivity.KEY_ADDRESS);
            if(addressInfo != null)
                address_edittext.setText(addressInfo.getAddressString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_join_address_edittext_textview:
                Intent intent = new Intent(getActivity(), AddressSearchActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.f_join_terms_textview:
                TempActivity.startTempActivity(this,TempActivity.TERMS);
                break;
            case R.id.f_join_send_btn:{
                    AppUtil.hideCurrentKeyboard(getActivity());
                    join();
                }
                break;
            case R.id.f_join_login_text:
                if(mActivityListener != null){
                    mActivityListener.doFragmentChange(new LoginFragment(),LoginFragment.class.getName(),R.anim.fade_in,R.anim.fade_out,true);
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

    public boolean emsCheck(EditText editText,int min,int max){
        if(min >= 0 && editText.getText().length() < min){
            return false;
        }else if(max >= 0 && editText.getText().length() > max){
            return false;
        }
        return true;
    }

    void join(){
        if(!terms_checkbox.isChecked()){
            showMessage(AppUtil.getXmlString(R.string.joinfragment_terms_check));
//        }else if(!emsCheck(name_edittext) || !name_edittext.getText().toString().matches(AppUtil.REGEX_HANGUL)){
//            showMessage(AppUtil.getXmlString(R.string.joinfragment_name_check));
        }else if(addressInfo.getZoneId() == 0){
            showMessage("주소를 입력해주세요.");
        }else if(!emsCheck(email_edittext,6,-1) || !email_edittext.getText().toString().matches(AppUtil.REGEX_EMAIL)){
            showMessage(AppUtil.getXmlString(R.string.joinfragment_email_check));
        }else if(!emsCheck(phone_edittext,10,11) || !phone_edittext.getText().toString().matches(AppUtil.REGEX_PHONE)){
            showMessage(AppUtil.getXmlString(R.string.joinfragment_phonenumber_check));
        }else if(!phone_authorization){
            showMessage(AppUtil.getXmlString(R.string.joinfragment_phone_authorization_check));
        }else if(!emsCheck(password_edittext,8,20) || !password_edittext.getText().toString().matches(AppUtil.REGEX_PASSWORD)){
            showMessage(AppUtil.getXmlString(R.string.joinfragment_password_check));
        }else if(!password_edittext.getText().toString().equals(password_confirm_edittext.getText().toString())){
            showMessage("두 패스워드가 같지 않습니다.");
        }else {
            NetworkManager.getInstance().userJoin(
                    email_edittext.getText().toString(),
                    password_edittext.getText().toString(),
                    "이름없음",
                    phone_edittext.getText().toString(),
                    addressInfo.getZoneId(),
                    NW_JOIN,
                    this);
        }
    }

    @Override
    public void onChecked(CustomCheckBox view, boolean before) {
        if(!before) {
            if (!emsCheck(phone_edittext, 10, 11) || !phone_edittext.getText().toString().matches(AppUtil.REGEX_PHONE))
                showMessage(AppUtil.getXmlString(R.string.joinfragment_phonenumber_check));
            else {
                AuthorizationDialogFragment authorizationDialogFragment = AuthorizationDialogFragment.newInstance(1234);
                authorizationDialogFragment.setAuthorizationCallback(this);
                authorizationDialogFragment.show(getFragmentManager(), AuthorizationDialogFragment.class.getName());
                //NetworkManager.getInstance().authorizationPhone(phone_edittext.getText().toString(),NW_AUTORIZATION,this);
            }
        }
        view.setChecked(before);
    }

    @Override
    public void setAuthorization(boolean authorization) {
        phone_authorization = authorization;
        authorization_button.setChecked(authorization);
        if(authorization) {
            phone_edittext.setEnabled(false);
            authorization_button.setEnabled(false);
            authorization_button.setText("인증 완료");
            showMessage(AppUtil.getXmlString(R.string.authorization_success));
        }
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){

            case NW_JOIN:
                if(success && object != null){
                    Result result = (Result)object;
                    if(result.result == 1) {
                        StorageManager storageManager = StorageManager.getInstance();
                        storageManager.setSettingValue("AUTOLOGIN", "T");
                        String id = email_edittext.getText().toString();
                        String pw = password_edittext.getText().toString();
                        storageManager.setSettingValue("ID", id);
                        storageManager.setSettingValue("PW", pw);
                        NetworkManager.getInstance().userLogin(id,pw,NW_LOGIN,this);
                    }else{
                        showMessage("네트워크 오류입니다.\n다시 한번 시도해주세요.");
                        Log.e("userjoin", result.toString());
                    }
                }
                break;

            case NW_LOGIN:
                if(success && object != null){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                break;

            case NW_AUTORIZATION:
                if(success && object != null){
                    Sms sms = (Sms)object;
                    if(sms.result == 1) {
                        AuthorizationDialogFragment authorizationDialogFragment = AuthorizationDialogFragment.newInstance(sms.tmp);
                        authorizationDialogFragment.setAuthorizationCallback(this);
                        authorizationDialogFragment.show(getFragmentManager(), AuthorizationDialogFragment.class.getName());
                    }else{
                        showMessage("문자 전송에 실패하였습니다.");
                        Log.e("userjoin", sms.toString());
                    }
                }
                break;

            default:
                super.onResponse(KEY,false,object);

        }
    }
}
