package com.dangjang.dj2015.main;

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
public class AddressSearchView extends FrameLayout {
    public AddressSearchView(Context context) {
        super(context);
        init();
    }

    public AddressSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    LinearLayout linearLayout;
    TextView addresstext_textview;
    ImageView delete_imageView;
    AddressInfo item;

    private void init() {
        inflate(getContext(), R.layout.view_address_search, this);
        linearLayout = (LinearLayout)findViewById(R.id.v_addresssearch_main_layout);
        addresstext_textview = (TextView)findViewById(R.id.v_addresssearch_addresstext_textview);
        delete_imageView = (ImageView)findViewById(R.id.v_addresssearch_delete_imageview);
    }

    public void setData(AddressInfo item) {
        addresstext_textview.setText(item.getAddressString());
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
