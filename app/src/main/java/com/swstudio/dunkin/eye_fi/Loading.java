package com.swstudio.dunkin.eye_fi;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;


public class Loading extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), CallActivity.class));
                finish();
            }
        }, 3000);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        int duration = 200;

        BitmapDrawable mBitmapDrawable_1 = (BitmapDrawable)getResources().getDrawable(R.drawable.loading1);
        BitmapDrawable mBitmapDrawable_2 = (BitmapDrawable)getResources().getDrawable(R.drawable.loading2);
        BitmapDrawable mBitmapDrawable_3 = (BitmapDrawable)getResources().getDrawable(R.drawable.loading3);
        BitmapDrawable mBitmapDrawable_4 = (BitmapDrawable)getResources().getDrawable(R.drawable.loading4);

        AnimationDrawable mAnimation = new AnimationDrawable();
        mAnimation.setOneShot(false);

        mAnimation.addFrame(mBitmapDrawable_1, duration);
        mAnimation.addFrame(mBitmapDrawable_2, duration);
        mAnimation.addFrame(mBitmapDrawable_3, duration);
        mAnimation.addFrame(mBitmapDrawable_4, duration);

        final ImageView img = (ImageView)findViewById(R.id.loadImage);
        img.setImageDrawable(mAnimation);
        mAnimation.start();
    }
}
