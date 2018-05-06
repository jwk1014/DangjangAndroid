package com.dangjang.dj2015.first;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.MyApplication;
import com.dangjang.dj2015.R;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class CustomCheckBox2 extends FrameLayout {
    private boolean checked = false;
    private boolean checkable = true;

    public CustomCheckBox2(Context context) {
        super(context);
        init();
    }

    public CustomCheckBox2(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] attrsArray = new int[]{
                R.attr.text,
                R.attr.textSize
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        text = ta.getString(0);
        textSize = ta.getDimension(1, 0)/MyApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        ta.recycle();
        init();
    }

    String text;
    float textSize;
    LinearLayout main_layout;
    ImageView imageview;
    TextView textView;

    private void init() {
        onCheckedListeners = new ArrayList<>();
        inflate(getContext(), R.layout.view_custom_check_box2, this);
        main_layout = (LinearLayout)findViewById(R.id.v_custom_check_box_main_layout);
        imageview = (ImageView)findViewById(R.id.v_custom_check_box_imageview);
        textView = (TextView)findViewById(R.id.v_custom_check_box_textview);
        if(text != null)
            textView.setText(text);
        if(textSize > 0)
            textView.setTextSize(textSize);
        main_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkable) {
                    boolean before = checked;
                    checked = !checked;
                    for (OnCheckedListener onCheckedListener : onCheckedListeners)
                        onCheckedListener.onChecked(CustomCheckBox2.this, before);
                    notifyCheckSetChanged();
                }
            }
        });
    }

    public void notifyCheckSetChanged(){
        if (checked) {
            imageview.setImageResource(R.drawable.checkbox_g);
            textView.setTextColor(AppUtil.getResourceColor(R.color.colorPrimary));
        } else {
            imageview.setImageResource(R.drawable.checkbox_grey);
            textView.setTextColor(AppUtil.getResourceColor(R.color.colorFontDisable));
        }
    }

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public void setChecked(boolean checked){
        this.checked = checked;
        notifyCheckSetChanged();
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public boolean isChecked() {
        return checked;
    }

    public interface OnCheckedListener{
        void onChecked(CustomCheckBox2 view, boolean before);
    }

    ArrayList<OnCheckedListener> onCheckedListeners;

    public void addOnCheckedListener(OnCheckedListener onCheckedListener) {
        onCheckedListeners.add(onCheckedListener);
    }
}
