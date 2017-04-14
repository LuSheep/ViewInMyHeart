package com.scu.lly.customviews.view.newstudyer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/4/13.
 */

public class LoadingViewActivity extends Activity {

    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingview);

        mLoadingView = (LoadingView) findViewById(R.id.id_loadingview);
    }

    public void startLoadingview(View view){
        mLoadingView.startAnim();
    }
}
