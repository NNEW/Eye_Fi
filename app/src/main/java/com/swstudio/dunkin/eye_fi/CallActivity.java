package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class CallActivity extends Activity {

    String dbName = "vltList.db";
    String tableName = "vltListTable";
    int dbMode = Context.MODE_PRIVATE;

    private ListView vltList;
    private ListView valueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        SharedPreferences prefs = getSharedPreferences("PrefName", Context.MODE_PRIVATE);
        String text = prefs.getString("Name", "");
        final EditText edit = (EditText)findViewById(R.id.Name);
        edit.setText(text);

        // Make DB Table
        GetGuideNum.db = openOrCreateDatabase(dbName, dbMode, null);
        String sql = "create table if not exists " + tableName + "(id integer primary key autoincrement, name text not null, phone text not null)";
        GetGuideNum.db.execSQL(sql);
        Log.d("DB", "DB Create");

        findViewById(R.id.callBtn).setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    final EditText edit = (EditText)findViewById(R.id.Name);
                    String myId = edit.getText().toString();
                    if(myId.equals("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "도움요청을 하지 못했습니다.\n사용자의 이름을 설정해주십시오.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        SmsManager mSmsManager = SmsManager.getDefault();
                        ArrayList<String> numbers = new ArrayList<String>();

                        String sql = "select * from " + tableName + ";";
                        GetGuideNum.db = openOrCreateDatabase(dbName, dbMode, null);
                        Cursor results = GetGuideNum.db.rawQuery(sql, null);

                        results.moveToFirst();

                        while (!results.isAfterLast()) {
                            String number = results.getString(2);

                            numbers.add(number);

                            results.moveToNext();
                        }
                        results.close();

                        for (int i = 0; i < numbers.size(); i++) {
                            String smsText = "[EYE-FI] " + myId + "님으로부터 도움요청.";
                            mSmsManager.sendTextMessage(numbers.get(i), null, smsText, null, null);
                        }

                        Toast toast = Toast.makeText(getApplicationContext(), numbers.size() + "명에게 도움요청을 했습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        });

        // DB Check
        findViewById(R.id.seeGuide).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });
    }

    protected void onStop() {
        super.onStop();
        final EditText edit = (EditText)findViewById(R.id.Name);
        String text = edit.getText().toString();

        // 데이타를저장합니다.
        SharedPreferences prefs = getSharedPreferences("PrefName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Name", text);
        editor.commit();

    }
}
