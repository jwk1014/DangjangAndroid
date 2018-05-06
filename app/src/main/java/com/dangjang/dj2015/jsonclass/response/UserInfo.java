package com.dangjang.dj2015.jsonclass.response;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class UserInfo extends Result implements Serializable{
    public String user_id;
    public String name;
    public String phone;
    public int zone_id;
    public String addr1;
    public String addr2;
    public String addr3;

    public String getAddress(){
        return addr1+" "+addr2+" "+addr3;
    }
}
