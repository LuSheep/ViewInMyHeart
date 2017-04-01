package com.scu.lly.customviews.view.aboutpaint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.scu.lly.customviews.R;

/**
 * 仿手势书写
 * Created by lusheep on 2017/3/28.
 */
public class GestrueFakeActivity extends Activity {

    private GestrueFakeView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestrueview);
        mView = (GestrueFakeView) findViewById(R.id.gustruefakeview);
    }

    public void reset(View view){
        mView.reset();
    }

}
