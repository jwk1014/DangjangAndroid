package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.MartProductItem;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class ProductListChildRowView extends FrameLayout implements View.OnClickListener{
    public ProductListChildRowView(Context context) {
        super(context);
        init();
    }

    public ProductListChildRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductListChildRowView(Context context,OnRowItemClickListener onRowItemClickListener){
        super(context);
        setOnRowItemClickListener(onRowItemClickListener);
    }

    public ProductListChildView views[];
    public LinearLayout mainlayout;
    MartProductItem items[];

    private void init() {
        inflate(getContext(), R.layout.view_product_child_row, this);
        mainlayout = (LinearLayout)findViewById(R.id.v_product_child_row_main_layout);
        views = new ProductListChildView[3];
        views[0] = (ProductListChildView)findViewById(R.id.v_productchildrow_item1);
        views[0].setOnClickListener(this);
        views[1] = (ProductListChildView)findViewById(R.id.v_productchildrow_item2);
        views[1].setOnClickListener(this);
        views[2] = (ProductListChildView)findViewById(R.id.v_productchildrow_item3);
        views[2].setOnClickListener(this);
    }

    public void setData(MartProductItem items[]) {
        for(int i=0;i<3;i++){
            if(i < items.length) {
                views[i].setVisibility(View.VISIBLE);
                views[i].setData(items[i]);
            }else
                views[i].setVisibility(View.INVISIBLE);
        }
        this.items = items;
    }

    @Override
    public void onClick(View v) {
        if(onRowItemClickListener != null) {
            switch (v.getId()) {
                case R.id.v_productchildrow_item1:
                    onRowItemClickListener.onRowItemClick(v,0);
                    break;
                case R.id.v_productchildrow_item2:
                    onRowItemClickListener.onRowItemClick(v,1);
                    break;
                case R.id.v_productchildrow_item3:
                    onRowItemClickListener.onRowItemClick(v,2);
                    break;
            }
        }
    }

    public void setMainLayoutMargin(int top,int bottom){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)mainlayout.getLayoutParams();
        layoutParams.topMargin = top;
        layoutParams.bottomMargin = bottom;
        mainlayout.setLayoutParams(layoutParams);
    }

    public void setContentsPadding(int top){
        for(ProductListChildView productListChildView:views){
            if(productListChildView.getVisibility() == View.VISIBLE){
                productListChildView.setPadding(
                        productListChildView.getPaddingLeft(),
                        top,
                        productListChildView.getPaddingRight(),
                        productListChildView.getPaddingBottom());
            }else
                break;
        }
    }

//    public void setContentIndexPadding(int index,int top,int bottom){
//        ProductListChildView productListChildView = views[index];
//        productListChildView.setPadding(
//                productListChildView.getPaddingLeft(),
//                top,
//                productListChildView.getPaddingRight(),
//                bottom);
//    }

    public interface OnRowItemClickListener{
        void onRowItemClick(View v,int index);
    }

    OnRowItemClickListener onRowItemClickListener;

    public void setOnRowItemClickListener(OnRowItemClickListener onRowItemClickListener) {
        this.onRowItemClickListener = onRowItemClickListener;
    }
}
