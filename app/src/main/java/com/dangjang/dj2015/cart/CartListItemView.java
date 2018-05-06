package com.dangjang.dj2015.cart;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.MartProductItem;

/**
 * Created by KJW on 2015-11-14.
 */
public class CartListItemView extends FrameLayout {
    public CartListItemView(Context context) {
        super(context);
        init();
    }
    public CartListItemView(Context context,boolean history) {
        super(context);
        this.history = history;
        init();
    }

    public CartListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView productImage;
    ImageButton deleteImageButton;
    TextView beforePrice,price,product_name,product_count,product_sumprice,off;
    OrderItem item;
    boolean history = false;

    private void init() {
        inflate(getContext(), R.layout.view_cartlistitem, this);
        LinearLayout main_layout = (LinearLayout)findViewById(R.id.v_cartlistitem_main_layout);
        main_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCustomClickListener != null)
                    onCustomClickListener.onClick(item);
            }
        });
        productImage = (ImageView)findViewById(R.id.v_cartlistitem_productimage_imageview);
        off = (TextView)findViewById(R.id.v_cartlistitem_off_textview);
        deleteImageButton = (ImageButton)findViewById(R.id.v_cartlistitem_productdelete_imagebutton);
        beforePrice = (TextView)findViewById(R.id.v_cartlistitem_beforeprice_textview);
        price = (TextView)findViewById(R.id.v_cartlistitem_price_textview);
        product_name = (TextView)findViewById(R.id.v_cartlistitem_productname_textview);
        product_count = (TextView)findViewById(R.id.v_cartlistitem_productcount_textview);
        product_sumprice = (TextView)findViewById(R.id.v_cartlistitem_productsumprice_textview);
        if(history) {
            deleteImageButton.setVisibility(View.INVISIBLE);
        }else {
            deleteImageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOrderDeleteListener != null)
                        onOrderDeleteListener.onOrderDelete(item);
                }
            });
        }
    }

    public void setData(OrderItem item) {
        this.item = item;
        String img_url = item.getMartProductItem().getProductItem().getImg_url();
        if(img_url != null && img_url.length() > 0)
            AppUtil.loadImage(getContext(), img_url, 0, productImage);
        else
            productImage.setImageResource(R.mipmap.ic_launcher);
        price.setText(AppUtil.getMoneyStr(item.getMartProductItem().getPrice()) + "원");
        product_sumprice.setText(AppUtil.getMoneyStr(item.getMartProductItem().getPrice() * item.getCount())+"원");
        if(item.getMartProductItem().getProductItem().getUnit() == null || item.getMartProductItem().getProductItem().getUnit().length() == 0)
            product_name.setText(item.getMartProductItem().getProductItem().getName());
        else
            product_name.setText(Html.fromHtml(item.getMartProductItem().getProductItem().getName() + " <font color='#999999'>/ " + item.getMartProductItem().getProductItem().getUnit() + "</font>"));
        product_count.setText("수량 : "+item.getCount());
    }

    public interface OnOrderDeleteListener{
        void onOrderDelete(OrderItem orderItem);
    }
    public interface OnCustomClickListener{
        void onClick(OrderItem orderItem);
    }

    OnOrderDeleteListener onOrderDeleteListener;
    OnCustomClickListener onCustomClickListener;

    public void setOnOrderDeleteListener(OnOrderDeleteListener onOrderDeleteListener) {
        this.onOrderDeleteListener = onOrderDeleteListener;
    }

    public void setOnCustomClickListener(OnCustomClickListener onCustomClickListener) {
        this.onCustomClickListener = onCustomClickListener;
    }
}
