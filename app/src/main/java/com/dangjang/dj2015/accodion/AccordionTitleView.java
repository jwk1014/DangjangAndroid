package com.dangjang.dj2015.accodion;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class AccordionTitleView extends FrameLayout {

    public AccordionTitleView(Context context) {
        super(context);
        init();
    }

    public AccordionTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView indicate_imageView;
    TextView name_textView,date_textView;
    AccordionTitleItem item;
    private void init() {
        inflate(getContext(), R.layout.view_accordion_title, this);
        indicate_imageView = (ImageView)findViewById(R.id.view_accodiontitle_indicate_textview);
        name_textView = (TextView)findViewById(R.id.view_accodiontitle_name_textview);
        date_textView = (TextView)findViewById(R.id.view_accodiontitle_date_textview);
    }

    public void setData(AccordionTitleItem item, boolean expand) {
        name_textView.setText(item.getName());
        if(item.getDate() != null)
            date_textView.setText(item.getDateString());
        else
            date_textView.setText("");
        if(expand)
            indicate_imageView.setImageResource(R.drawable.uparrow);
        else
            indicate_imageView.setImageResource(R.drawable.downarrow);
        this.item = item;
    }
}