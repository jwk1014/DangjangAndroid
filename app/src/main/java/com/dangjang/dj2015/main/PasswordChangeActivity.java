package com.dangjang.dj2015.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import org.w3c.dom.Text;

public class PasswordChangeActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String KEY_CURRENT_PASSWORD = "CURRENT_PASSWORD";
    public static final String KEY_CHANGE_PASSWORD = "CHANGE_PASSWORD";
    EditText current_edittext,change_edittext,changeConfirm_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        setTitle(AppUtil.getXmlString(R.string.passwordchangeactivity_title));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView email_textview = (TextView)findViewById(R.id.a_passwordchange_email_textview);
        if(getIntent() != null){
            Intent intent = getIntent();
            email_textview.setText(intent.getStringExtra("userid"));
        }

        current_edittext = (EditText)findViewById(R.id.a_passwordchange_currentpassword_edittext);
        change_edittext = (EditText)findViewById(R.id.a_passwordchange_changepassword_edittext);
        changeConfirm_edittext = (EditText)findViewById(R.id.a_passwordchange_changepasswordconfirm_edittext);

        findViewById(R.id.a_passwordchange_button).setOnClickListener(this);

//        change_edittext.setAutoValidate(true);
//        change_edittext.addValidator(new METValidator(getResources().getString(R.string.joinfragment_password_error)) {
//            @Override
//            public boolean isValid(CharSequence text, boolean isEmpty) {
//                return (isEmpty || (text.length() >= 8 && text.length() <= 30 && (text.toString().matches(AppUtil.REGEX_PASSWORD))));
//            }
//        });
//        changeConfirm_edittext.setAutoValidate(true);
//        changeConfirm_edittext.addValidator(new METValidator("변경할 패스워드와 일치하지 않습니다.") {
//            @Override
//            public boolean isValid(CharSequence text, boolean isEmpty) {
//                return (isEmpty || (change_edittext.getText().toString().equals(changeConfirm_edittext.getText().toString())));
//            }
//        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(ResultCode.RESULT_CANCEL);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(ResultCode.RESULT_CANCEL);
        super.onBackPressed();
    }

    public boolean emsCheck(EditText editText,int min,int max){
        if(min >= 0 && editText.getText().length() < min){
            return false;
        }else if(max >= 0 && editText.getText().length() > max){
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        /*if(!emsCheck(current_edittext,8,20) || !current_edittext.getText().toString().matches(AppUtil.REGEX_PASSWORD)) {
            Toast.makeText(this, "현재 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else */
        if(!emsCheck(change_edittext, 8, 20) || !change_edittext.getText().toString().matches(AppUtil.REGEX_PASSWORD)){
            Toast.makeText(this, "변경할 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else if(!change_edittext.getText().toString().equals(changeConfirm_edittext.getText().toString())) {
            Toast.makeText(this, "변경할 확인 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
            intent.putExtra(KEY_CURRENT_PASSWORD, current_edittext.getText().toString());
            intent.putExtra(KEY_CHANGE_PASSWORD, change_edittext.getText().toString());
            setResult(ResultCode.RESULT_OK,intent);
            finish();
        }
    }
}
