package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class BackCategoryView extends FrameLayout {
    public BackCategoryView(Context context) {
        super(context);
        init();
    }

    public BackCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    String item;
    TextView textView;

    private void init() {
        inflate(getContext(), R.layout.view_back_category, this);
        textView = (TextView)findViewById(R.id.v_backcategory_textview);
    }

    public void setData(String item) {
        textView.setText(item);
        this.item = item;
    }
}