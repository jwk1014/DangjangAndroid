package com.dangjang.dj2015.productlist;

import com.dangjang.dj2015.categoryselect.CategorySelectChildStringItem;
import com.dangjang.dj2015.publicdata.MartProductItem;

import java.util.List;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class ProductListItem {
    private CategorySelectChildStringItem categoryName;
    private List<MartProductItem> productItems;

    public CategorySelectChildStringItem getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategorySelectChildStringItem categoryName) {
        this.categoryName = categoryName;
    }

    public List<MartProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<MartProductItem> productItems) {
        this.productItems = productItems;
    }
}
