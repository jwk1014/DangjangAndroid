package com.dangjang.dj2015.cart;

import com.dangjang.dj2015.publicdata.MartItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class CartItem implements Serializable{
    private int id;
    private MartItem martItem;
    private List<OrderItem> orderItems;
    private int totalcost;
    private Date orderDate;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MartItem getMartItem() {
        return martItem;
    }

    public void setMartItem(MartItem martItem) {
        this.martItem = martItem;
    }

    public int getTotalcost() {
        return totalcost;
    }

    public void setTotalcost(int totalcost) {
        this.totalcost = totalcost;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }
}
