package com.scu.lly.customviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.view.newstudyer.PaopaoPopActivity;

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
}
