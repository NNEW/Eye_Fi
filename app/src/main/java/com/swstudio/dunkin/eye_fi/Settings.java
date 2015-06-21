package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.setHelp).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), dbList.class));
            }
        });

        findViewById(R.id.setVideo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Context context = getApplicationContext();
                int status = NetworkUtil.getConnectivityStatus(context);
                if (status == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setIconAttribute(android.R.attr.alertDialogIcon);
                    alert.setTitle("네트워크 연결 실패");
                    alert.setMessage("네트워크 확인 후 다시 실행해주세요.");
                    alert.show();
                } else {
                    if (isSkypeClientInstalled(getApplicationContext())) {
                        if (status == 1) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    skype("echo123", getApplicationContext());
                                }
                            });
                            alert.setIconAttribute(android.R.attr.alertDialogIcon);
                            alert.setTitle("영상 통화 설정 완료");
                            alert.setMessage("Skype 테스트용 통화에 연결합니다.");
                            alert.show();
                        } else {
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Settings.this);
                            alert_confirm
                                    .setIconAttribute(android.R.attr.alertDialogIcon)
                                    .setTitle("3G/4G 데이터 사용 중")
                                    .setMessage("통화 연결시 데이터 소모가 생길 수도 있습니다.\nSkype 테스트용 통화에 연결하시겠습니까?")
                                    .setCancelable(false)
                                    .setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    skype("echo123", getApplicationContext());
                                                }
                                            })
                                    .setNegativeButton("취소",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                            AlertDialog alert = alert_confirm.create();
                            alert.show();
                        }
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToMarket(context);
                            }
                        });
                        alert.setIconAttribute(android.R.attr.alertDialogIcon);
                        alert.setTitle("Skype 어플리케이션 없음");
                        alert.setMessage("해당 서비스는 Skype 어플리케이션이 필요합니다.\n마켓으로 이동합니다.");
                        alert.show();
                    }
                }
            }
        });
    }

    public static boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);

    }
    public static void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }

    public static void skype(String number, Context ctx) {
        try {
            //Intent sky = new Intent("android.intent.action.CALL_PRIVILEGED");
            //the above line tries to create an intent for which the skype app doesn't supply public api

            Uri skypeUri = Uri.parse("skype:" + number + "?call&video=true");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

            // Restrict the Intent to being handled by the Skype for Android client only.
            myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Initiate the Intent. It should never fail because you've already established the
            // presence of its handler (although there is an extremely minute window where that
            // handler can go away).
            ctx.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("SKYPE CALL", "Skype failed", e);
        }

    }
}
