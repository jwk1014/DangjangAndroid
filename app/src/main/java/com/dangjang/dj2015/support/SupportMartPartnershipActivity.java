package com.dangjang.dj2015.support;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.parentclass.AppActivity;

public class SupportMartPartnershipActivity extends AppActivity {

    public final static int NW_PARTNERSHIP = 1;

    EditText martname_edittext,address_edittext,phone_edittext,admin_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_mart_partnership);
        //setTitle(AppUtil.getXmlString(R.string.supportmartpartnershipactivity_title));
        setTitle("고객 센터");
        setSnackBarContainer(R.id.a_supportmartpartnership_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        martname_edittext = (EditText)findViewById(R.id.a_supportmartpartnership_martname_edittext);
        address_edittext = (EditText)findViewById(R.id.a_supportmartpartnership_address_edittext);
        phone_edittext = (EditText)findViewById(R.id.a_supportmartpartnership_phone_edittext);
        admin_edittext = (EditText)findViewById(R.id.a_supportmartpartnership_admin_edittext);
        if(getIntent() != null){
            Intent intent = getIntent();
            UserInfo userinfo = (UserInfo)intent.getSerializableExtra("userinfo");
            if(userinfo != null){
                phone_edittext.getText().append(userinfo.phone);
                admin_edittext.getText().append(userinfo.name);
            }
        }
        Button submit = (Button)findViewById(R.id.a_supportmartpartnership_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(martname_edittext.getText().length() == 0){
                    snackbarShow("마트이름을 입력해주세요.");
                }else if(address_edittext.getText().length() == 0){
                    snackbarShow("위치를 입력해주세요.");
                }else if(phone_edittext.getText().length() == 0){
                    snackbarShow("연락처를 입력해주세요.");
                }else if(admin_edittext.getText().length() == 0){
                    snackbarShow("담당자 이름을 입력해주세요.");
                }else if(!admin_edittext.getText().toString().trim().matches(AppUtil.REGEX_HANGUL)){
                    snackbarShow("담당자 이름을 올바르게 입력해주세요.");
                }else{
                    NetworkManager.getInstance().partnership(
                            martname_edittext.getText().toString(),
                            address_edittext.getText().toString(),
                            phone_edittext.getText().toString(),
                            admin_edittext.getText().toString(),
                            NW_PARTNERSHIP,
                            SupportMartPartnershipActivity.this
                    );
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){
            case NW_PARTNERSHIP:
                if(success && object != null){
                    Result result = (Result)object;
                    if(result.result == 1){
                        Toast.makeText(this, "성공적으로 문의하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                        snackbarShow("네트워크 오류");
                }else
                    snackbarShow("네트워크 오류");
                break;
            default:
                super.onResponse(KEY, success, object);
        }
    }
}
