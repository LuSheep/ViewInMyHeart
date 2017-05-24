package com.scu.lly.customviews.view.newstudyer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/4/14.
 */

public class MaterialLoadingViewActivity extends Activity {

    private MaterialLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materialloadingview);

        mView = (MaterialLoadingView) findViewById(R.id.id_materialloadingview);
    }

    public void showMaterialView(View view){
        mView.startAnim();
    }
}
