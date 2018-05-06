package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.R;
import com.dangjang.dj2015.categoryselect.CategorySelectChildStringItem;

/**
 * Created by KJW on 2015-11-14.
 */
public class ProductCategoryView extends FrameLayout{
    public ProductCategoryView(Context context) {
        super(context);
        init();
    }

    public ProductCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    CategorySelectChildStringItem item;
    TextView categoryName;
    ImageView more;

    private void init() {
        inflate(getContext(), R.layout.view_product_parent, this);
        categoryName = (TextView)findViewById(R.id.v_productparentitem_categoryname_textview);
        more = (ImageView)findViewById(R.id.v_productparentitem_more_imageview);
    }

    public void setData(CategorySelectChildStringItem item) {
        categoryName.setText(item.getString());
        this.item = item;
    }
}
