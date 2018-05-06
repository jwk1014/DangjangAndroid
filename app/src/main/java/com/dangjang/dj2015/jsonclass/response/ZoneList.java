package com.dangjang.dj2015.jsonclass.response;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class ZoneList extends Result{
    public int zone_id;
    public String addr1;
    public String addr2;
    public String addr3;
    public String getAddressString(){
        return addr1+" "+addr2+" "+addr3;
    }
}
