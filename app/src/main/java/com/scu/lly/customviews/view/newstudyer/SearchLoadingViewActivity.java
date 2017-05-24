package com.scu.lly.customviews.view.newstudyer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/4/20.
 */

public class SearchLoadingViewActivity extends Activity {

    private SearchLoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchloadingview);

        loadingView = (SearchLoadingView) findViewById(R.id.id_searchloadingview);
    }

    public void startSearchLoadingview(View view){
        loadingView.startAnim();
    }

    public void stopSearchLoadingview(View view){
        loadingView.stopAnim();
    }
}
