package com.dangjang.dj2015.cart;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class CartListItemBottomView extends FrameLayout {

    public CartListItemBottomView(Context context) {
        super(context);
        init();
    }

    public CartListItemBottomView(Context context,boolean history) {
        super(context);
        this.history = history;
        init();
    }

    public CartListItemBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    int cart_id, delivery_cost, total_price, martid;
    TextView delivery_price_textview, total_price_textview, delivery_request_textview;
    ImageView delete_imageview;
    String martname;
    boolean history = false;

    private void init() {
        inflate(getContext(), R.layout.view_cartlistitem_bottom, this);
        delivery_price_textview = (TextView)findViewById(R.id.v_cartlistitem_bottom_deliverycost_textview);
        total_price_textview = (TextView)findViewById(R.id.v_cartlistitem_bottom_productsumprice_textview);
        delivery_request_textview = (TextView)findViewById(R.id.v_cartlistitem_bottom_deliveryrequest_textview);
        delete_imageview = (ImageView)findViewById(R.id.v_cartlistitem_bottom_productdelete_imageview);

        if(history) {
            delete_imageview.setVisibility(View.INVISIBLE);
            delivery_request_textview.setVisibility(View.GONE);
        }else{
            delivery_request_textview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeliveryRequestListener != null)
                        onDeliveryRequestListener.onDeliveryRequest(cart_id, total_price, martid, martname);
                }
            });

            delete_imageview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCartDeleteListener != null)
                        onCartDeleteListener.onCartDelete(cart_id, martid, martname);
                }
            });
        }
    }

    public void setData(int cart_id, int delivery_cost, int total_price, int martid, String martname) {
        this.cart_id = cart_id;
        this.delivery_cost = delivery_cost;
        this.total_price = total_price;
        this.martid = martid;
        this.martname = martname;
        delivery_price_textview.setText("배달비 : "+AppUtil.getMoneyStr(((total_price >= 10000 + delivery_cost)?0:delivery_cost))+"원\n(1만원 이상 무료배달)");
        if(total_price >= 10000 + delivery_cost)
            total_price_textview.setText(AppUtil.getMoneyStr(total_price - delivery_cost) + " 원");
        else
            total_price_textview.setText(AppUtil.getMoneyStr(total_price)+" 원");
    }

    public interface OnDeliveryRequestListener{
        void onDeliveryRequest(int cart_id,int total_price,int mart_id,String martname);
    }

    OnDeliveryRequestListener onDeliveryRequestListener;

    public void setOnDeliveryRequestListener(OnDeliveryRequestListener onDeliveryRequestListener) {
        this.onDeliveryRequestListener = onDeliveryRequestListener;
    }

    public interface OnCartDeleteListener{
        void onCartDelete(int cart_id,int mart_id,String martname);
    }

    OnCartDeleteListener onCartDeleteListener;

    public void setOnCartDeleteListener(OnCartDeleteListener onCartDeleteListener) {
        this.onCartDeleteListener = onCartDeleteListener;
    }
}