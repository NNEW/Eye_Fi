package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PopupActivity extends Activity {

    private String notiMessage;
    private String notiNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bun = getIntent().getExtras();
        notiMessage = bun.getString("notiMessage");
        notiNum = bun.getString("notiNum");

        setContentView(R.layout.activity_popup);

        TextView adMessage = (TextView)findViewById(R.id.message);
        adMessage.setText("'" + notiMessage + "'님으로부터\n도움요청이 왔습니다.\n전화연결하시겠습니까?");

        findViewById(R.id.cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        findViewById(R.id.call).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // LG electronics phone.
                        startActivity(new Intent("com.lge.ims.action.VT_REQUEST")
                                .putExtra("com.lge.ims.extra.VT_PHONE_NUMBER_LIST", new String[] { notiNum })
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                }
        );

    }
}