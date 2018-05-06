package com.dangjang.dj2015.productdetail;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
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
public class CustomCheckBox extends FrameLayout {
    private boolean checked = false;

    public CustomCheckBox(Context context) {
        super(context);
        init();
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] attrsArray = new int[]{
                R.attr.text,
                R.attr.textSize,
                R.attr.backgroundVisible
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        text = ta.getString(0);
        textSize = ta.getDimension(1, 0)/MyApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        if(!ta.getBoolean(2,true)){
            invisible = true;
        }
        ta.recycle();
        init();
    }

    String text;
    float textSize;
    boolean invisible;
    LinearLayout main_layout;
    ImageView imageview;
    TextView textView;

    private void init() {
        onCheckedListeners = new ArrayList<>();
        inflate(getContext(), R.layout.view_custom_check_box, this);
        main_layout = (LinearLayout)findViewById(R.id.v_custom_check_box_main_layout);
        imageview = (ImageView)findViewById(R.id.v_custom_check_box_imageview);
        textView = (TextView)findViewById(R.id.v_custom_check_box_textview);
        if(text != null)
            textView.setText(text);
        if(textSize > 0)
            textView.setTextSize(textSize);
        if(invisible) {
//            if(Build.VERSION.SDK_INT < 16)
//                main_layout.setBackgroundDrawable(Color.TRANSPARENT);
//            else
//                main_layout.setBackground(drawable);
            main_layout.setBackgroundColor(Color.TRANSPARENT);
        }
        main_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean before = checked;
                checked = !checked;
                for(OnCheckedListener onCheckedListener:onCheckedListeners)
                    onCheckedListener.onChecked(CustomCheckBox.this,before);
                notifyCheckSetChanged();
            }
        });
    }

    public void notifyCheckSetChanged(){
        if (checked) {
            if(!invisible)
                main_layout.setBackgroundResource(R.drawable.roundedbox_g);
            imageview.setImageResource(R.drawable.checkmark_g);
            textView.setTextColor(AppUtil.getResourceColor(R.color.colorPrimary));
        } else {
            if(!invisible)
                main_layout.setBackgroundResource(R.drawable.roundedbox_grey);
            imageview.setImageResource(R.drawable.checkmark_grey);
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

    public boolean isChecked() {
        return checked;
    }

    public interface OnCheckedListener{
        void onChecked(CustomCheckBox view,boolean before);
    }

    ArrayList<OnCheckedListener> onCheckedListeners;

    public void addOnCheckedListener(OnCheckedListener onCheckedListener) {
        onCheckedListeners.add(onCheckedListener);
    }
}
