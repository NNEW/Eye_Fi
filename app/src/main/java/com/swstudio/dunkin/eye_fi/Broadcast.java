package com.swstudio.dunkin.eye_fi;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;


public class Broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            // sms 수신
            // SMS 메시지를 파싱합니다.
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

// SMS 수신 시간 확인
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.d("문자 수신 시간", curDate.toString());

// SMS 발신 번호 확인
            String origNumber = smsMessage[0].getOriginatingAddress();

// SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();
            if (message.contains("[EYE-FI]")) {
                bundle.putString("notiMessage", message.substring(9, message.indexOf("님으로부터")));
                bundle.putString("notiNum", origNumber);
                showNotification(context, bundle);
            }
            Log.d("문자 내용", "발신자 : " + origNumber + ", 내용 : " + message);
        }
    }

    private void showNotification(Context context, Bundle bune) {
        Intent popupIntent = new Intent(context, PopupActivity.class);

        popupIntent.putExtras(bune);
        PendingIntent pie= PendingIntent.getActivity(context, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
        try {
            pie.send();
        } catch (PendingIntent.CanceledException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }
    }
}
