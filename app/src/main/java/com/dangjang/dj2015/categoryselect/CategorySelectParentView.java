package com.dangjang.dj2015.categoryselect;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.MyApplication;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectParentView extends FrameLayout {
    public final static int OPEN = 1;
    public final static int CLOSE = 0;

    public CategorySelectParentView(Context context) {
        super(context);
        init();
    }

    public CategorySelectParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView dropdown_imageView;
    TextView title_textView,select_textView;
    CategorySelectItem item;
    private void init() {
        inflate(getContext(), R.layout.view_category_select_parent, this);
        dropdown_imageView = (ImageView)findViewById(R.id.category_parent_dropdown_imageview_id);
        title_textView = (TextView)findViewById(R.id.category_parent_title_textview_id);
        select_textView = (TextView)findViewById(R.id.category_parent_select_textview_id);
        setOpenDropdownImage(false);
        setClickable(true);
    }

    public void setData(CategorySelectItem item) {
        title_textView.setText(item.name);
        setOpenDropdownImage(false);
        this.item = item;
        refresh();
    }

    public int getSize(){
        return item.getContent().size();
    }

    public void refresh(){
        CategorySelectChildItem child = item.content.get(item.select);
        if(child instanceof CategorySelectChildMartItem){
            String result;
            if(item.select == 0) {
                result = "전체";
                select_textView.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
            }else {
                result = ((CategorySelectChildMartItem) item.getContent().get(item.select)).getMartName();
                select_textView.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorFont));
            }
            select_textView.setText(result);
//            for(CategorySelectChildItem temp:item.content){
//                CategorySelectChildMartItem temp_child = (CategorySelectChildMartItem)temp;
//                if(temp_child.check) {
//                    result = temp_child.martName;
//                    break;
//                }
//            }
//            if(result.length() == 0) {
//                item.select = 0;
//                select_textView.setText("전체");
//                select_textView.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
//            }else {
//                select_textView.setText(result.substring(0, result.length() - 1));
//            }
        }else {
            String text = ((CategorySelectChildStringItem) child).string;
            if(text.equals("전체"))
                select_textView.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
            else
                select_textView.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorFont));
            select_textView.setText(text);
        }
    }

    public void setOpenDropdownImage(boolean open){
        int drawable_id = (open)?R.drawable.uparrow:R.drawable.downarrow;
        if(dropdown_imageView.getTag() == null || ((Integer)dropdown_imageView.getTag()) != drawable_id) {
            dropdown_imageView.setImageResource(drawable_id);
            dropdown_imageView.setTag(drawable_id);
        }
    }

    public boolean getOpenState(){
        return (((Integer)dropdown_imageView.getTag()) != R.drawable.downarrow);
    }

    public  int toggleDropdownImage(){
        if(((Integer)dropdown_imageView.getTag()) == R.drawable.downarrow) {
            setOpenDropdownImage(true);
            return OPEN;
        }else {
            setOpenDropdownImage(false);
            return CLOSE;
        }
    }

    public int getCurrentType(){
        return item.getCurrentType();
    }
}