package com.dangjang.dj2015.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.dangjang.dj2015.R;
import com.dangjang.dj2015.first.SplashActivity;
import com.dangjang.dj2015.main.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

/**
 * Created by Tacademy on 2015-10-15.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("gcm.notification.title");
        String body = data.getString("gcm.notification.body");
//        String message = data.getString("message");
//        if(title == null){
//            String msg = data.getString("msg");
//            Log.i("msg",msg);
//            Gson gson = new Gson();
//            MessageData mData = gson.fromJson(msg, MessageData.class);
//            title = mData.title;
//            destination = mData.destination;
//            message = mData.message;
//        }

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Body: " + body);
//        Log.d(TAG, "Destination: " + destination);
//        Log.d(TAG, "Message: " + message);
//        try {
//            if (title.equals("sendSMS")) {
//                SmsManager mSmsManager = SmsManager.getDefault();
//                mSmsManager.sendTextMessage(destination, null, message, null, null);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, body);
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message) {

        //Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), SplashActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);
                //.setSound(defaultSoundUri)

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
