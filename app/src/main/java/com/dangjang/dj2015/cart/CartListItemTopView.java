package com.dangjang.dj2015.cart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.MartItem;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class CartListItemTopView extends FrameLayout {
    public CartListItemTopView(Context context) {
        super(context);
        init();
    }

    public CartListItemTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    MartItem item;
    TextView martname_textview,delivery_time_textview;
    ImageView martdetail_textview;

    private void init() {
        inflate(getContext(), R.layout.view_cartlistitem_top, this);
        martname_textview = (TextView)findViewById(R.id.v_cartlistitem_top_martname_textview);
        delivery_time_textview = (TextView)findViewById(R.id.v_cartlistitem_top_deliverytime_textview);
        martdetail_textview = (ImageView)findViewById(R.id.v_cartlistitem_top_martdetail_imageview);
        martdetail_textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMartDetailListener != null)
                    onMartDetailListener.onMartDetail(item.getId());
            }
        });
    }

    public void setData(MartItem item) {
        this.item = item;
        martname_textview.setText(item.getName());
        delivery_time_textview.setText("배달 가능 시간 :  오전 10시 ~ 오후 6시");
    }

    public interface OnMartDetailListener{
        void onMartDetail(int mart_id);
    }

    OnMartDetailListener onMartDetailListener;

    public void setOnMartDetailListener(OnMartDetailListener onMartDetailListener) {
        this.onMartDetailListener = onMartDetailListener;
    }
}