package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.R;

import java.util.zip.Inflater;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class AddressDisplayView extends FrameLayout {
    public AddressDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddressDisplayView(Context context) {
        super(context);
        init();
    }

    String item,temp;
    TextView textView;

    private void init() {
        inflate(getContext(), R.layout.view_address_display, this);
        textView = (TextView)findViewById(R.id.v_addressdisplay_textview);
    }

    public void setTempData(String item){
        temp = item;
    }

    public void setTempApply(){
        if(temp != null) {
            setData(temp);
            temp = null;
        }
    }

    public void setData(String item) {
        textView.setText("배달지 : "+item);
        this.item = item;
    }
}