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

import java.util.Date;

public class SupportOneononeActivity extends AppActivity {

    public final static int NW_CONTACT = 1;

    EditText name_edittext,email_edittext,content_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_oneonone_ask);
        setSnackBarContainer(R.id.a_supportoneonone_main_layout);
        //setTitle(AppUtil.getXmlString(R.string.supportoneononeactivity_title));
        setTitle("고객 센터");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name_edittext = (EditText)findViewById(R.id.a_supportoneonone_name_edittext);
        email_edittext = (EditText)findViewById(R.id.a_supportoneonone_email_edittext);
        if(getIntent() != null){
            Intent intent = getIntent();
            UserInfo userinfo = (UserInfo)intent.getSerializableExtra("userinfo");
            if(userinfo != null){
                name_edittext.getText().append(userinfo.name);
                email_edittext.getText().append(userinfo.user_id);
            }
        }
        content_edittext = (EditText)findViewById(R.id.a_supportoneonone_content_edittext);
        Button submit = (Button)findViewById(R.id.a_supportoneonone_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_edittext.getText().length() == 0){
                    snackbarShow("이름을 입력해주세요.");
                }else if(!name_edittext.getText().toString().matches(AppUtil.REGEX_HANGUL)){
                    snackbarShow("올바른 한글 이름을 입력해주세요.");
                }else if(email_edittext.getText().length() == 0){
                    snackbarShow("이메일을 입력해주세요.");
                }else if(!email_edittext.getText().toString().matches(AppUtil.REGEX_EMAIL)){
                    snackbarShow("올바른 이메일 주소를 입력해주세요.");
                }else if(content_edittext.getText().length() == 0){
                    snackbarShow("문의 내용을 입력해주세요.");
                }else{
                    NetworkManager.getInstance().contact(
                            name_edittext.getText().toString(),
                            email_edittext.getText().toString(),
                            content_edittext.getText().toString(),
                            new Date(),
                            NW_CONTACT,
                            SupportOneononeActivity.this
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
            case NW_CONTACT:
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
