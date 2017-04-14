package com.scu.lly.customviews.view.newstudyer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/4/14.
 */

public class WeixinEyesActivity extends Activity {

    private WeixinEyesView mEyesView;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixineyes);

        mEyesView = (WeixinEyesView) findViewById(R.id.id_weixineyes);
        mSeekBar = (SeekBar) findViewById(R.id.id_seekbar_weixineyes);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mEyesView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
