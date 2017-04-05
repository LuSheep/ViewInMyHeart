package com.scu.lly.customviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.view.aboutpaint.ClockViewActivity;
import com.scu.lly.customviews.view.aboutpaint.GestrueFakeActivity;
import com.scu.lly.customviews.view.aboutpaint.RadialGradientActivity;
import com.scu.lly.customviews.view.aboutpaint.ShadowBitmapActivity;
import com.scu.lly.customviews.view.aboutpaint.BitmapShaderDemoActivity;
import com.scu.lly.customviews.view.aboutpaint.ShimmerTextActivity;
import com.scu.lly.customviews.view.aboutpaint.TestPaintActivity;
import com.scu.lly.customviews.view.aboutpaint.WaterWaveActivity;
import com.scu.lly.customviews.view.aboutpaint.XfermodeActivity;

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

    public void gestureViewDemo(View view){
        Intent i = new Intent(this, GestrueFakeActivity.class);
        startActivity(i);
    }

    public void waterWaveDemo(View view){
        Intent i = new Intent(this, WaterWaveActivity.class);
        startActivity(i);
    }

    public void xfermodeDemo(View view){
        Intent i = new Intent(this, XfermodeActivity.class);
        startActivity(i);
    }

    public void shaderBitmapDemo(View view){
        Intent i = new Intent(this, ShadowBitmapActivity.class);
        startActivity(i);
    }

    public void bitmapShaderDemo(View view){
        Intent i = new Intent(this, BitmapShaderDemoActivity.class);
        startActivity(i);
    }

    public void linearGradientDemo(View view){
        Intent i = new Intent(this, ShimmerTextActivity.class);
        startActivity(i);
    }

    public void radialGradientDemo(View view){
        Intent i = new Intent(this, RadialGradientActivity.class);
        startActivity(i);
    }
}
