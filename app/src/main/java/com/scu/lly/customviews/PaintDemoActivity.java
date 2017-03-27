package com.scu.lly.customviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.view.aboutpaint.ClockViewActivity;
import com.scu.lly.customviews.view.aboutpaint.TestPaintActivity;

/**
 * Created by lusheep on 2017/3/26.
 */

public class PaintDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintdemo);
    }

    public void testPaintDemo(View view){
        Intent i = new Intent(this, TestPaintActivity.class);
        startActivity(i);
    }

    public void clockViewDemo(View view){
        Intent i = new Intent(this, ClockViewActivity.class);
        startActivity(i);
    }
}
