package com.scu.lly.customviews.view.anim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/3/30.
 */

public class TestAnimActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testanim);
    }

    public void testanim(View view){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_test);
        view.startAnimation(anim);
    }
}
