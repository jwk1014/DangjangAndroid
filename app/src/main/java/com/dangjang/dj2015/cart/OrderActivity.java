package com.dangjang.dj2015.cart;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.first.CustomCheckBox2;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.main.AddressSearchActivity;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.productdetail.CustomCheckBox;
import com.dangjang.dj2015.productdetail.MartDetailActivity;
import com.dangjang.dj2015.publicdata.AddressInfo;

import org.w3c.dom.Text;

import java.util.Date;

public class OrderActivity extends AppActivity implements View.OnClickListener{
    public final static String KEY_CARTITEM = "CARTITEM";
    CartItem cartItem;
    UserInfo userInfo;

    public final static int NW_USERINFO = 1;
    public final static int NW_ORDERCHECK = 2;

    Spinner spinner;
    EditText name_edittext,address_edittext,other_address_edittext,phone_edittext,request_edittext,pointcard_edittext;
    CustomCheckBox delivery_home_checkbox,delivery_mart_checkbox,buy_another_checkbox,buy_no_checkbox,buy_phone_checkbox,pay_method_card_checkbox,pay_method_cash_checkbox;
    CustomCheckBox2 pay_method_cash_tax_checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle(AppUtil.getXmlString(R.string.orderactivity_title));
        setSnackBarContainer(R.id.a_order_main_layout);

        NetworkManager.getInstance().userInfo(NW_USERINFO, this, true, true);

        Intent intent = getIntent();
        cartItem = (CartItem)intent.getSerializableExtra(KEY_CARTITEM);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_cost = new TextView(this);
        if(cartItem.getTotalcost() >= 10000 + cartItem.getMartItem().getDeliveryCost())
            toolbar_cost.setText(AppUtil.getMoneyStr(cartItem.getTotalcost() - cartItem.getMartItem().getDeliveryCost()) + " 원");
        else
            toolbar_cost.setText(AppUtil.getMoneyStr(cartItem.getTotalcost()) + "원");
        toolbar_cost.setTextSize(AppUtil.getDp(R.dimen.textsize_10sp));
        toolbar_cost.setTextColor(Color.WHITE);
        toolbar_cost.setTypeface(null, Typeface.BOLD);
        ActionBar.LayoutParams toolbar_layoutparams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        toolbar_layoutparams.setMargins(0, 0, AppUtil.getDp(R.dimen.margin_6dp), 0);
        getSupportActionBar().setCustomView(toolbar_cost, toolbar_layoutparams);

        TextView martName = (TextView)findViewById(R.id.a_order_top_martname_textview);
        martName.setText(cartItem.getMartItem().getName());

        ImageView imageView = (ImageView)findViewById(R.id.a_order_top_martdetail_imageview);
        imageView.setOnClickListener(this);
        spinner = (Spinner)findViewById(R.id.a_order_delivery_time_spinner);
        name_edittext = (EditText)findViewById(R.id.a_order_name_edittext);
        address_edittext = (EditText)findViewById(R.id.a_order_address_edittext);
        TextView textView = (TextView)findViewById(R.id.a_order_address_click_textview);
        textView.setOnClickListener(this);
        ImageButton imageButton = (ImageButton)findViewById(R.id.a_order_address_button);
        imageButton.setOnClickListener(this);
        other_address_edittext = (EditText)findViewById(R.id.a_order_address_edittext);
        phone_edittext = (EditText)findViewById(R.id.a_order_phone_edittext);
        delivery_home_checkbox = (CustomCheckBox)findViewById(R.id.a_order_delivery_home_check);
        delivery_mart_checkbox = (CustomCheckBox)findViewById(R.id.a_order_delivery_mart_check);
        buy_another_checkbox = (CustomCheckBox)findViewById(R.id.a_order_buy_another_checkbox);
        buy_no_checkbox = (CustomCheckBox)findViewById(R.id.a_order_buy_no_checkbox);
        buy_phone_checkbox = (CustomCheckBox)findViewById(R.id.a_order_buy_phone_checkbox);
        request_edittext = (EditText)findViewById(R.id.a_order_request_edittext);
        pay_method_card_checkbox = (CustomCheckBox)findViewById(R.id.a_order_pay_method_card_checkbox);
        final ImageView cash_background = (ImageView)findViewById(R.id.a_order_pay_method_cash_background_imageview);
        pay_method_cash_checkbox = (CustomCheckBox)findViewById(R.id.a_order_pay_method_cash_checkbox);
        pay_method_card_checkbox.addOnCheckedListener(new CustomCheckBox.OnCheckedListener() {
            @Override
            public void onChecked(CustomCheckBox view, boolean before) {
                if(!before) {
                    cash_background.setImageResource(R.drawable.roundedbox_grey);
                    pay_method_cash_tax_checkbox.setChecked(false);
                    pay_method_cash_tax_checkbox.setCheckable(false);
                }
            }
        });
        pay_method_cash_checkbox.addOnCheckedListener(new CustomCheckBox.OnCheckedListener() {
            @Override
            public void onChecked(CustomCheckBox view, boolean before) {
                if(!before) {
                    cash_background.setImageResource(R.drawable.roundedbox_g);
                    pay_method_cash_tax_checkbox.setCheckable(true);
                }
            }
        });
        pay_method_cash_tax_checkbox = (CustomCheckBox2)findViewById(R.id.a_order_pay_methde_cash_tax_checkbox);
        pay_method_cash_tax_checkbox.setCheckable(false);
        pointcard_edittext = (EditText)findViewById(R.id.a_order_pointcard_edittext);
        Button submit = (Button)findViewById(R.id.a_order_button);
        submit.setOnClickListener(this);
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
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ResultCode.RESULT_OK:
                AddressInfo addressInfo = (AddressInfo)data.getSerializableExtra(AddressSearchActivity.KEY_ADDRESS);
                address_edittext.setText(addressInfo.getAddressString());
                userInfo.zone_id = addressInfo.getZoneId();
                break;
            case ResultCode.RESULT_FAIL:
                break;
            case ResultCode.RESULT_CANCEL:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.a_order_top_martdetail_imageview: {
                Intent intent = new Intent(this, MartDetailActivity.class);
                intent.putExtra("mart_id", cartItem.getMartItem().getId());
                startActivity(intent);
            }
                break;
            case R.id.a_order_address_click_textview:
            case R.id.a_order_address_button: {
                Intent intent = new Intent(this, AddressSearchActivity.class);
                startActivityForResult(intent, 1);
            }
                break;
            case R.id.a_order_button:{
                if(name_edittext.getText().length() == 0){
                    snackbarShow("성명을 입력해주세요.");
                }else if(!name_edittext.getText().toString().matches(AppUtil.REGEX_HANGUL)){
                    snackbarShow("성명을 올바르게 입력해주세요.");
                }else if(address_edittext.getText().length() == 0){
                    snackbarShow("주소를 입력해주세요.");
                }else if(other_address_edittext.getText().length() == 0){
                    snackbarShow("나머지 주소를 입력해주세요.");
                }else if(phone_edittext.getText().length() == 0){
                    snackbarShow("연락처를 입력해주세요.");
                }else if(!phone_edittext.getText().toString().matches(AppUtil.REGEX_PHONE)){
                    snackbarShow("연락처를 올바르게 입력해주세요.(휴대폰번호)");
                }else{
                    int place = 0;
                    if(delivery_home_checkbox.isChecked())
                        place = 1;
                    else if(delivery_mart_checkbox.isChecked())
                        place = 2;
                    int sold_out = 0;
                    if(buy_another_checkbox.isChecked())
                        sold_out = 1;
                    else if(buy_no_checkbox.isChecked())
                        sold_out = 2;
                    else if(buy_phone_checkbox.isChecked())
                        sold_out = 3;
                    int pay_methode = 0;
                    if(pay_method_card_checkbox.isChecked())
                        pay_methode = 1;
                    else if(pay_method_cash_checkbox.isChecked() && !pay_method_cash_tax_checkbox.isChecked())
                        pay_methode = 2;
                    else if(pay_method_cash_checkbox.isChecked() && pay_method_cash_tax_checkbox.isChecked())
                        pay_methode = 3;
                    NetworkManager.getInstance().orderCheck(
                            cartItem.getId(),
                            cartItem.getMartItem().getId(),
                            new Date(),
                            name_edittext.getText().toString(),
                            address_edittext.getText().toString()+" "+other_address_edittext.getText().toString(),
                            phone_edittext.getText().toString(),
                            place,
                            sold_out,
                            request_edittext.getText().toString(),
                            pay_methode,
                            pointcard_edittext.getText().toString(),
                            NW_ORDERCHECK,
                            this
                    );
                }
            }
                break;
        }
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){
            case NW_USERINFO:
                if(success && object != null) {
                    userInfo = (UserInfo)object;
                    name_edittext.getText().append(userInfo.name);
                    address_edittext.setText(userInfo.getAddress());
                    phone_edittext.getText().append(userInfo.phone);
                }
                break;
            case NW_ORDERCHECK:
                if(success && object != null) {
                    Result result = (Result)object;
                    if(result.result == 1) {
                        Toast.makeText(this, "성공적으로 주문되었습니다.", Toast.LENGTH_SHORT).show();
                        setResult(ResultCode.RESULT_OK);
                        finish();
                    }else
                        snackbarShow("주문에 실패하였습니다.");
                }else
                    snackbarShow("주문에 실패하였습니다.");
                break;
            default:
                super.onResponse(KEY, success, object);
        }
    }
}
