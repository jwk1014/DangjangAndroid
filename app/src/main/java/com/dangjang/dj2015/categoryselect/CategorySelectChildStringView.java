package com.dangjang.dj2015.categoryselect;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.MyApplication;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectChildStringView extends FrameLayout {
    public CategorySelectChildStringView(Context context) {
        super(context);
        init();
    }

    public CategorySelectChildStringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView textView;
    CategorySelectChildStringItem item;

    private void init() {
        inflate(getContext(), R.layout.view_category_select_child_string, this);
        textView = (TextView) findViewById(R.id.category_select_child_string_textview);
    }

    public void setData(CategorySelectChildStringItem item) {
        textView.setText(item.string);
        textView.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorFont));
        textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        this.item = item;
    }

    public void setAlignLeft(){
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }
    public void setTextColorGreen(){

        textView.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
    }
}
