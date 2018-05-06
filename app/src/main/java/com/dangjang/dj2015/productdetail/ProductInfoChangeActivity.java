package com.dangjang.dj2015.productdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.MartProductItem;

public class ProductInfoChangeActivity extends AppCompatActivity implements CustomCheckBox.OnCheckedListener{
    CustomCheckBox customCheckBox[];
    EditText editText;
    String temp;
    int customCheckBoxIds[] = new int[]{
            R.id.a_productinfochange_checkbox1,
            R.id.a_productinfochange_checkbox2,
            R.id.a_productinfochange_checkbox3,
            R.id.a_productinfochange_checkbox4,
            R.id.a_productinfochange_checkbox5,
            R.id.a_productinfochange_checkbox6,
            R.id.a_productinfochange_checkbox7
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info_change);
        setTitle(AppUtil.getXmlString(R.string.productinfochangeactivity_title));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent() != null && getIntent().getSerializableExtra("martProductItem") != null) {
            MartProductItem martProductItem = (MartProductItem)getIntent().getSerializableExtra("martProductItem");
            TextView martname = (TextView) findViewById(R.id.a_productinfochange_martname_textview);
            martname.setText(martProductItem.getMartItem().getName());
            TextView productname = (TextView) findViewById(R.id.a_productinfochange_productname_textview);
            productname.setText(martProductItem.getProductItem().getName());
        }
        customCheckBox = new CustomCheckBox[customCheckBoxIds.length];
        for(int i=0;i<customCheckBoxIds.length;i++){
            customCheckBox[i] = (CustomCheckBox)findViewById(customCheckBoxIds[i]);
            customCheckBox[i].addOnCheckedListener(this);
        }
        editText = (EditText)findViewById(R.id.a_productinfochange_edittext);
        editText.setEnabled(false);
        customCheckBox[0].setChecked(true);
        customCheckBox[0].notifyCheckSetChanged();
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
    public void onChecked(CustomCheckBox view, boolean before) {
        if(!before) {
            int view_id = view.getId();
            if(view_id == customCheckBoxIds[6]) {
                editText.setEnabled(true);
                editText.setBackgroundResource(R.drawable.roundedbox_g);
                if(temp != null)
                    editText.getText().append(temp);
            }else if(editText.isEnabled()) {
                editText.setEnabled(false);
                editText.setBackgroundResource(R.drawable.roundedbox_grey);
                temp = editText.getText().toString();
                editText.setText("");
            }
        }
    }
}
