package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.AddressInfo;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class ProductSearchView extends FrameLayout {
    public ProductSearchView(Context context) {
        super(context);
        init();
    }

    public ProductSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    LinearLayout linearLayout;
    TextView producttext_textview;
    ImageView delete_imageView;
    String item;

    private void init() {
        inflate(getContext(), R.layout.view_product_search, this);
        linearLayout = (LinearLayout)findViewById(R.id.v_productsearch_main_layout);
        producttext_textview = (TextView)findViewById(R.id.v_productsearch_producttext_textview);
        delete_imageView = (ImageView)findViewById(R.id.v_productsearch_delete_imageview);
    }

    public void setData(String item) {
        producttext_textview.setText(item);
        this.item = item;
    }

    public void setDeleteVisible(boolean b){
        if(b && delete_imageView.getVisibility() == View.INVISIBLE){
            delete_imageView.setVisibility(View.VISIBLE);
        }else if(!b && delete_imageView.getVisibility() == View.VISIBLE){
            delete_imageView.setVisibility(View.INVISIBLE);
        }
    }

    public void setOnCustomClickListener(OnClickListener onCustomClickListener) {
        linearLayout.setOnClickListener(onCustomClickListener);
    }

    public void setOnDeleteClickListener(OnClickListener onDeleteClickListener) {
        delete_imageView.setOnClickListener(onDeleteClickListener);
    }
}
