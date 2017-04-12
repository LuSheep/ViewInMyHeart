package com.scu.lly.customviews.view.newstudyer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/4/9.
 */

public class ProgressAnimActivity extends Activity {

    private ProgressAnimView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressanim);

        progressView = (ProgressAnimView) findViewById(R.id.id_progressanim);
    }

    public void startProgress(View view){
        progressView.start();
    }
}
