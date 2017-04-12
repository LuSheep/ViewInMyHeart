package com.scu.lly.customviews.view.newstudyer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * 时速盘
 * Created by lusheep on 2017/4/10.
 */

public class SpeedPanelActivity extends Activity {

    private SpeedPanelView mPanelView;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedpanelview);

        mPanelView = (SpeedPanelView) findViewById(R.id.id_speedpanelview);
    }

    public void goPanel(View view){
        progress += 5;
        mPanelView.setProgress(progress);
    }
}
