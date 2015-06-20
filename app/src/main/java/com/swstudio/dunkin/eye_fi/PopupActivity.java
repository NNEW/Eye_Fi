package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RemoteViews;
import android.widget.TextView;

public class PopupActivity extends Activity {

    private String notiMessage;
    private String notiNum;
    private boolean check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bun = getIntent().getExtras();
        notiMessage = bun.getString("notiMessage");
        notiNum = bun.getString("notiNum");

        setContentView(R.layout.activity_popup);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(ns);

        PendingIntent pendingCallIntent = PendingIntent.getActivity(this, 0, new Intent("com.lge.ims.action.VT_REQUEST")
                .putExtra("com.lge.ims.extra.VT_PHONE_NUMBER_LIST", new String[]{notiNum})
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);

        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext())
                .setContentTitle("EYE-FI 도움요청")
                .setContentText(notiMessage + "님으로부터 도움요청이 왔습니다.")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setTicker("EYE-FI 도움요청")
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_help));

        Notification mNoti = mBuilder.build();
        mNoti.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(9999, mNoti);
        TextView adMessage = (TextView) findViewById(R.id.message);
        adMessage.setText(" " + notiMessage + "님으로부터\n도움요청이 왔습니다.\n전화연결하시겠습니까?");

        findViewById(R.id.cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check = true;
                        finish();
                    }
                }
        );

        findViewById(R.id.call).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // LG electronics phone.
                        check = true;
                        startActivity(new Intent("com.lge.ims.action.VT_REQUEST")
                                .putExtra("com.lge.ims.extra.VT_PHONE_NUMBER_LIST", new String[]{notiNum})
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                }
        );
    }

    @Override
    public void finish() {
        super.finish();
        NotificationManager nNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nNM.cancel(9999);

        if(check) {
            check = false;
        } else {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager notificationManager = (NotificationManager) getSystemService(ns);

            PendingIntent pendingCallIntent = PendingIntent.getActivity(this, 0, new Intent("com.lge.ims.action.VT_REQUEST")
                    .putExtra("com.lge.ims.extra.VT_PHONE_NUMBER_LIST", new String[]{notiNum})
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);

            Notification.Builder mBuilder = new Notification.Builder(getApplicationContext())
                    .setContentTitle("EYE-FI 도움요청 (보류)")
                    .setContentText(notiMessage + "님과 전화연결을 원하시면 눌러주세요.")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_call))
                    .setTicker("EYE-FI 도움요청")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingCallIntent);

            Notification mNoti = mBuilder.build();
            notificationManager.notify(9999, mNoti);
        }
    }
}


