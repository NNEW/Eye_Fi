package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class CallActivity extends Activity {

    String dbName = "vltList.db";
    String tableName = "vltListTable";
    int dbMode = Context.MODE_PRIVATE;

    SQLiteDatabase db;

    private ListView vltList;
    private ListView valueList;

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
                                .putExtra("com.lge.ims.extra.VT_PHONE_NUMBER_LIST", new String[]{((EditText) findViewById(R.id.phoneNum)).getText().toString()})
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
        );

        // Call SetGuideNumber
        findViewById(R.id.setGuide).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(getApplication(), GetGuideNum.class));
                    }
                }
        );

        // DB Check
        findViewById(R.id.seeGuide).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), dbList.class));
            }
        });
    }
}
