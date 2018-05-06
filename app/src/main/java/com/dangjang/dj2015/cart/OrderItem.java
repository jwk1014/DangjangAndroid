package com.dangjang.dj2015.cart;

import com.dangjang.dj2015.publicdata.MartProductItem;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class OrderItem implements Serializable{
    private int id;
    private MartProductItem martProductItem;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MartProductItem getMartProductItem() {
        return martProductItem;
    }

    public void setMartProductItem(MartProductItem martProductItem) {
        this.martProductItem = martProductItem;
    }
}
