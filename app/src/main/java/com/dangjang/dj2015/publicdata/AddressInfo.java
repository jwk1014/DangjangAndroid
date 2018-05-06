package com.dangjang.dj2015.publicdata;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class AddressInfo implements Serializable{
    private String addressString;
    private long lat;
    private long lng;
    private int zoneId;

    public AddressInfo(int zoneId, String addressString) {
        this.zoneId = zoneId;
        this.addressString = addressString;
    }

    public AddressInfo(String addressString, long lat, long lng) {
        this.addressString = addressString;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public void setZoneId(int zoneId){
        this.zoneId = zoneId;
    }

    public int getZoneId(){
        return zoneId;
    }
}
