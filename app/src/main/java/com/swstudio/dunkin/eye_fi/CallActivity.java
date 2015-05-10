package com.swstudio.dunkin.eye_fi;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class CallActivity extends ActionBarActivity {

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

        // Call SetGuideNumber
        findViewById(R.id.setGuide).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(getApplication(), GetGuideNum.class));
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
