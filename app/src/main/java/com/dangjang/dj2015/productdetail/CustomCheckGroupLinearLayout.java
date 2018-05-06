package com.dangjang.dj2015.productdetail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-20.
 */
public class CustomCheckGroupLinearLayout extends LinearLayout implements CustomCheckBox.OnCheckedListener{
    ArrayList<CustomCheckBox> customCheckBoxes = new ArrayList<>();

    public CustomCheckGroupLinearLayout(Context context) {
        super(context);
    }

    public CustomCheckGroupLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        inChild(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        inChild(child);
    }

    private void inChild(View child){
        if(child instanceof CustomCheckBox){
            CustomCheckBox customCheckBox = (CustomCheckBox) child;
            if (getChildCount() == 1)
                customCheckBox.setChecked(true);
            customCheckBox.addOnCheckedListener(this);
            customCheckBoxes.add(customCheckBox);
        }
    }

    @Override
    public void onChecked(CustomCheckBox view, boolean before) {
        if(before){
           view.setChecked(true);
        }else{
            for(CustomCheckBox customCheckBox:customCheckBoxes){
                if(customCheckBox.isChecked() && customCheckBox != view) {
                    customCheckBox.setChecked(false);
                    break;
                }
            }
        }
    }
}
