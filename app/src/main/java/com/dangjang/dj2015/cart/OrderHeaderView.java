package com.dangjang.dj2015.cart;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class OrderHeaderView extends FrameLayout {

    public OrderHeaderView(Context context) {
        super(context);
        init();
    }

    public OrderHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    CartItem item;
    TextView martname_textview,date_textview;
    ImageView indicate_imageview;

    private void init() {
        inflate(getContext(), R.layout.view_order_history_header, this);
        martname_textview = (TextView)findViewById(R.id.view_orderhistory_name_textview);
        date_textview = (TextView)findViewById(R.id.view_orderhistory_date_textview);
        indicate_imageview = (ImageView)findViewById(R.id.view_orderhistory_indicate_imageview);
    }

    public void setData(CartItem item) {
        this.item = item;
        martname_textview.setText(item.getMartItem().getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        date_textview.setText(simpleDateFormat.format(item.getOrderDate()));
        //date_textview.setText();
    }
    public void indicateExpand(boolean expand){
        if(expand)
            indicate_imageview.setImageResource(R.drawable.uparrow);
        else
            indicate_imageview.setImageResource(R.drawable.downarrow);
    }
}
