package com.dangjang.dj2015.jsonclass.response;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class MartDetail extends Result {
    public int mart_id;
    public String name;
    public String addr;
    public String phone;
    public String ad1;
    public String ad2;
    public String hollyday;
    public class Location{
        public float x;
        public float y;
    }
    public Location location;
    public int zone_id;
    public String addr1;
    public String addr2;
    public String addr3;

    public String getAddressText(){
        return addr1 + " " + addr2 + " " + addr3;
    }
}
