package com.scu.lly.customviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.view.newstudyer.ChoutiMenuActivity;
import com.scu.lly.customviews.view.newstudyer.CouponTestActivity;
import com.scu.lly.customviews.view.newstudyer.PaopaoPopActivity;
import com.scu.lly.customviews.view.newstudyer.ProgressAnimActivity;
import com.scu.lly.customviews.view.newstudyer.SpeedPanelActivity;

/**
 * 新手自定义View实践系列
 * Created by lusheep on 2017/4/8.
 */

public class NewStudyerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstudyer);
    }

    public void paopaoDemo(View view){
        Intent i = new Intent(this, PaopaoPopActivity.class);
        startActivity(i);
    }

    public void progressAnimDemo(View view){
        Intent i = new Intent(this, ProgressAnimActivity.class);
        startActivity(i);
    }

    public void bolangviewDemo(View view){
        Intent i = new Intent(this, CouponTestActivity.class);
        startActivity(i);
    }

    public void chouticaidanDemo(View view){
        Intent i = new Intent(this, ChoutiMenuActivity.class);
        startActivity(i);
    }

    public void speedPanelDemo(View view){
        Intent i = new Intent(this, SpeedPanelActivity.class);
        startActivity(i);
    }
}
