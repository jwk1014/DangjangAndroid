package com.dangjang.dj2015.publicdata;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class MartItem implements Serializable{
    private int id;
    private String name;
    private String img_url;
    private AddressInfo address;
    private DeliveryTime[] deliveryTimes;
    private String restDays;
    private String phoneNumber;
    private String deliveryArea;
    private int deliveryCost;
    private String freeDeliveryCost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public AddressInfo getAddress() {
        return address;
    }

    public void setAddress(AddressInfo address) {
        this.address = address;
    }

    public DeliveryTime[] getDeliveryTimes() {
        return deliveryTimes;
    }

    public void setDeliveryTimes(DeliveryTime[] deliveryTimes) {
        this.deliveryTimes = deliveryTimes;
    }

    public String getRestDays() {
        return restDays;
    }

    public void setRestDays(String restDays) {
        this.restDays = restDays;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getFreeDeliveryCost() {
        return freeDeliveryCost;
    }

    public void setFreeDeliveryCost(String freeDeliveryCost) {
        this.freeDeliveryCost = freeDeliveryCost;
    }
}
