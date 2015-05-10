package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Date;


public class CallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        // Video call with phoneNumber.
        findViewById(R.id.callBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // LG electronics phone.
                        startActivity(new Intent("com.lge.ims.action.VT_REQUEST")
                                .putExtra("com.lge.ims.extra.VT_PHONE_NUMBER_LIST", new String[] { ((EditText)findViewById(R.id.phoneNum)).getText().toString() })
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
        );
    }
}
