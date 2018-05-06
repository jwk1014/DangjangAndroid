package com.dangjang.dj2015.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.dangjang.dj2015.Manager.NetworkManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static int network_state;
    public static boolean online;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                network_state = NetworkManager.getNetworkState();
        }/*
        if(Build.VERSION.SDK_INT < 21 && !network_state.equals(DISCONNECT)){
            new Thread(){
                @Override
                public void run() {
                    int networkType = 0;
                    switch (network_state){
                        case MOBILE:
                            networkType = ConnectivityManager.TYPE_MOBILE;
                            break;
                        case WIFI:
                            networkType = ConnectivityManager.TYPE_WIFI;
                            break;
                        case BLUETOOTH:
                            networkType = connectivityManager.TYPE_BLUETOOTH;
                            break;
                    }
                    int addr = 0;
                    try{
                        InetAddress inetAddress = InetAddress.getByName("www.google.com");
                        byte[] addrBytes = inetAddress.getAddress();
                        addr = (((addrBytes[3] & 0xff) << 24) | ((addrBytes[2] & 0xff) << 16) | ((addrBytes[1] & 0xff) << 8) | (addrBytes[0] & 0xff));
                    }catch (UnknownHostException e){
                        e.printStackTrace();
                    }
                    online = connectivityManager.requestRouteToHost(networkType,addr);
                }
            }.start();
        }else if(Build.VERSION.SDK_INT >= 21 && !network_state.equals(DISCONNECT)){
            online = true;
        }else
            online = false;
        if(online)
            Toast.makeText(context, "online", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "offline", Toast.LENGTH_SHORT).show();
            */
    }
}