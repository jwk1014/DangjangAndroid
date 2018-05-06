package com.dangjang.dj2015.accodion;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class AccordionContentView extends FrameLayout {
    public AccordionContentView(Context context) {
        super(context);
        init();
    }

    public AccordionContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView textView;
    AccordionContentItem item;

    private void init() {
        inflate(getContext(), R.layout.view_accordion_content, this);
        textView = (TextView) findViewById(R.id.view_accodioncontent_textview);
    }

    public void setData(AccordionContentItem item) {
        if(item.getSpanned() == null)
            textView.setText(item.getContent());
        else
            textView.setText(item.getSpanned());
        this.item = item;
    }
}
