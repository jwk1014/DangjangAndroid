package com.dangjang.dj2015.publicdata;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class MartProductItem implements Serializable{
    private ProductItem productItem;
    private MartItem martItem;
    private int salePrice=0;
    private int price;

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public MartItem getMartItem() {
        return martItem;
    }

    public void setMartItem(MartItem martItem) {
        this.martItem = martItem;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
