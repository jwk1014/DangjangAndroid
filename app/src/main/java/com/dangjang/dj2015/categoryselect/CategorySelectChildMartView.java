package com.dangjang.dj2015.categoryselect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectChildMartView extends FrameLayout {
    public CategorySelectChildMartView(Context context) {
        super(context);
        init();
    }

    public CategorySelectChildMartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView textView;
    ImageView imageView;
    CategorySelectChildMartItem item;

    private void init() {
        inflate(getContext(), R.layout.view_category_select_child_mart, this);
        textView = (TextView) findViewById(R.id.category_select_child_mart_name_textview);
        imageView = (ImageView) findViewById(R.id.category_select_child_mart_info_imageview);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMartInfoClickListener != null)
                    onMartInfoClickListener.onMartInfoClick(item.martId);
            }
        });
    }

    public void setData(CategorySelectChildMartItem item) {
        this.item = item;
        textView.setText(item.martName);
//        checkBox.setText(item.martName);
//        textView.setText(item.deliveryString);
//        if(item.check)
//            checkBox.setChecked(true);
//        else
//            checkBox.setChecked(false);
    }

    public interface OnMartInfoClickListener{
        void onMartInfoClick(int mart_id);
    }

    OnMartInfoClickListener onMartInfoClickListener;

    public void setOnMartInfoClickListener(OnMartInfoClickListener onMartInfoClickListener) {
        this.onMartInfoClickListener = onMartInfoClickListener;
    }

    //    public void toggleCheck(){
//        checkBox.setChecked(!item.check);
//        item.check = !item.check;
//    }
}
