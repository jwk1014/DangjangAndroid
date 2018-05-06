package com.dangjang.dj2015.productdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.jsonclass.response.CartId;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.publicdata.MartProductItem;

import org.w3c.dom.Text;

/**
 * Created by KJW on 2015-11-17.
 */
public class ProductDetailActivity extends AppActivity implements View.OnClickListener{

    public final static int NW_CARTSELECT = 1;
    public final static int NW_CARTSELECTID = 2;
    public final static int NW_ORDERDELETE = 3;
    private MartProductItem item;
    private EditText count_edittext;
    private TextView final_textview;
    private int order_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_detail2);
        setSnackBarContainer(R.id.a_productdetail_main_layout);
        setAutoBackPressed(false);

        Intent intent = getIntent();
        item = (MartProductItem)intent.getSerializableExtra("item");

        StorageManager.getInstance().addHistoryProduct(item.getProductItem().getId());

        setTitle(item.getMartItem().getName());

        ImageView imageView = (ImageView)findViewById(R.id.f_productdetail2_product_imageview);
        AppUtil.loadImage(this, item.getProductItem().getImg_url(), 0, imageView);

        TextView name_textview = (TextView)findViewById(R.id.f_productdetail2_productname_textview);
        if(item.getProductItem().getUnit() == null || item.getProductItem().getUnit().length() == 0)
            name_textview.setText(item.getProductItem().getName());
        else
            name_textview.setText(Html.fromHtml(item.getProductItem().getName() + " <font color='#999999'>/ " + item.getProductItem().getUnit() + "</font>"));

        count_edittext = (EditText)findViewById(R.id.f_productdetail2_count_edittext);
        final_textview = (TextView)findViewById(R.id.f_productdetail2_final_price_textview);
        if(intent.getIntExtra("count",0) > 0) {
            count_edittext.getText().append("" + intent.getIntExtra("count", 0));
            final_textview.setText(AppUtil.getMoneyStr(item.getPrice() * intent.getIntExtra("count", 0)) + "원");
        }else {
            count_edittext.getText().append("1");
            final_textview.setText(AppUtil.getMoneyStr(item.getPrice()) + "원");
        }
        count_edittext.addTextChangedListener(new TextWatcher() {
            CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after > 0)
                    temp = s.subSequence(start, count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    int value = Integer.parseInt(count_edittext.getText().toString());
                    if (value > 100) {
                        Toast.makeText(ProductDetailActivity.this, "100개를 초과해서 주문할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        count_edittext.getText().replace(start, start + count, temp);
                    } else if (value < 1) {
                        Toast.makeText(ProductDetailActivity.this, "1개 미만으로 주문할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        count_edittext.getText().replace(start, start + count, temp);
                    }
                } else if (s.length() == 0) {
                    count_edittext.getText().append("1");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        TextView price_textview = (TextView)findViewById(R.id.f_productdetail2_price_textview);
        price_textview.setText(AppUtil.getMoneyStr(item.getPrice()) + "원");
        TextView before_price_textView = (TextView)findViewById(R.id.f_productdetail2_beforeprice_textview);
        before_price_textView.setVisibility(View.VISIBLE);
        before_price_textView.setText(AppUtil.getMoneyStr(item.getPrice()*10/9/10*10)+"원");

        TextView textView = (TextView)findViewById(R.id.f_productdetail2_productinfochange_textview);
        textView.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.f_productdetail2_productpricechange_textview);
        textView.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.f_productdetail2_put_textview);
        if(intent.getIntExtra("order_id",0) > 0){
            order_id = intent.getIntExtra("order_id",0);
            textView.setText("장바구니 상품 수량 변경");
        }
        textView.setOnClickListener(this);
        ImageButton imageButton = (ImageButton)findViewById(R.id.f_productdetail2_countup_imagebutton);
        imageButton.setOnClickListener(this);
        imageButton = (ImageButton)findViewById(R.id.f_productdetail2_countdown_imagebutton);
        imageButton.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.martinfor_w);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this,MartDetailActivity.class);
                intent.putExtra("mart_id",item.getMartItem().getId());
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        layoutParams.setMargins(0,0,AppUtil.getDp(R.dimen.margin_6dp),0);
        getSupportActionBar().setCustomView(imageView,layoutParams);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        super.onBackPressed();
        setResult(ResultCode.RESULT_CANCEL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_productdetail2_productpricechange_textview:{
                Intent intent = new Intent(this, ProductPriceChangeActivity.class);
                intent.putExtra("martProductItem",item);
                startActivity(intent);
            }
            break;
            case R.id.f_productdetail2_productinfochange_textview: {
                Intent intent = new Intent(this, ProductInfoChangeActivity.class);
                intent.putExtra("martProductItem",item);
                startActivity(intent);
            }
            break;
            case R.id.f_productdetail2_countup_imagebutton:{
                int value = Integer.parseInt(count_edittext.getText().toString())+1;
                if(value > 100) {
                    Toast.makeText(this, "100개를 초과해서 주문할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Editable editable = count_edittext.getText();
                    editable.replace(0, editable.length(), "" + (value));
                    final_textview.setText(AppUtil.getMoneyStr((value) * item.getPrice()) + "원");
                }
            }
            break;
            case R.id.f_productdetail2_countdown_imagebutton:{
                int value = Integer.parseInt(count_edittext.getText().toString())-1;
                if(value < 1) {
                    Toast.makeText(this, "1개 미만으로 주문할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Editable editable = count_edittext.getText();
                    editable.replace(0, editable.length(), "" + (value));
                    final_textview.setText(AppUtil.getMoneyStr((value) * item.getPrice()) + "원");
                }
            }
            break;
            case R.id.f_productdetail2_put_textview:{
                if(order_id == 0)
                    NetworkManager.getInstance().cartSelectId(NW_CARTSELECTID, this);
                else
                    NetworkManager.getInstance().cartDelete(order_id, NW_ORDERDELETE, this);
            }
            break;
        }
    }
    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){
            case NW_CARTSELECTID:
                if(success && object != null)  {
                    CartId cartId  = (CartId)object;
                    if(cartId.result != 0){
                        NetworkManager.getInstance().cartSelect(
                                cartId.cart_id,
                                item.getMartItem().getId(),
                                item.getProductItem().getId(),
                                Integer.parseInt(count_edittext.getText().toString()),
                                NW_CARTSELECT,
                                this);
                    }else
                        snackbarShow("장바구니 담기에 실패하였습니다.");
                }
                break;
            case NW_CARTSELECT:
                if(success && object != null)  {
                    Result result = (Result)object;
                    if(result.result == 1) {
                        setResult(ResultCode.RESULT_OK);
                        finish();
                    }else
                        snackbarShow(result.msg);
                }else
                    snackbarShow("장바구니 담기에 실패하였습니다.");
                break;
            case NW_ORDERDELETE:
                if(success && object != null)  {
                    Result result = (Result)object;
                    if(result.result == 1) {
                        NetworkManager.getInstance().cartSelectId(NW_CARTSELECTID, this);
                    }else
                        snackbarShow(result.msg);
                }else
                    snackbarShow("장바구니 담기에 실패하였습니다.");
                break;
            default:
                super.onResponse(KEY,false,object);
        }
    }
}
