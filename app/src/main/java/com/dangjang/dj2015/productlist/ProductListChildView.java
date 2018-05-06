package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.MartProductItem;
import com.dangjang.dj2015.publicdata.ProductItem;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class ProductListChildView extends FrameLayout {
    public ProductListChildView(Context context) {
        super(context);
        init();
    }

    public ProductListChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView productImage;
    TextView beforePrice,price,nameInfo,martname,off;
    MartProductItem item;

    private void init() {
        inflate(getContext(), R.layout.view_product_child, this);
        martname = (TextView)findViewById(R.id.v_productchilditem_mart_textview);
        productImage = (ImageView)findViewById(R.id.v_productchilditem_productimage_imageview);
        off = (TextView)findViewById(R.id.v_productchilditem_off_textview);
        beforePrice = (TextView)findViewById(R.id.v_productchilditem_beforeprice_textview);
        price = (TextView)findViewById(R.id.v_productchilditem_price_textview);
        nameInfo = (TextView)findViewById(R.id.v_productchilditem_nameinfo_textview);
    }

    public void setData(MartProductItem item) {
        String img_url = item.getProductItem().getImg_url();
        if(img_url != null && img_url.length() > 0)
            AppUtil.loadImage(getContext(),img_url,0,productImage);
        else
            productImage.setImageResource(R.drawable.unloadedimage);
        martname.setText(item.getMartItem().getName());
        //martImage.setImageResource(R.mipmap.ic_launcher);
        //offImage.setImageResource(R.mipmap.ic_launcher);
        //deliveryImage.setImageResource(R.mipmap.ic_launcher);
        if(item.getSalePrice() == 0)
            price.setText(AppUtil.getMoneyStr(item.getPrice())+"원");
        else{
            beforePrice.setText(AppUtil.getMoneyStr(item.getPrice())+"원");
            price.setText(AppUtil.getMoneyStr(item.getSalePrice())+"원");
        }
        ProductItem productItem = item.getProductItem();
        if(productItem.getUnit() == null || productItem.getUnit().length() == 0)
            nameInfo.setText(productItem.getName());
        else{
            nameInfo.setText(Html.fromHtml(productItem.getName() + " <font color='#999999'>/ " + productItem.getUnit() + "</font>"));
        }
        this.item = item;
    }
}
