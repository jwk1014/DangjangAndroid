package com.dangjang.dj2015.Manager;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class PhoneManager {
    private static PhoneManager manager;
    TelephonyManager telephonyManager;
    private PhoneManager(Context context){
        telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }
    public static PhoneManager getInstance(){
        if(manager == null){
            manager = new PhoneManager(MyApplication.getContext());
        }
        return manager;
    }
    public String getPhoneNumber(){
        String phoneNumber = null;
        try{
            phoneNumber = telephonyManager.getLine1Number();
        }catch (Exception e){
            e.printStackTrace();
        }
        return phoneNumber;
    }
}
