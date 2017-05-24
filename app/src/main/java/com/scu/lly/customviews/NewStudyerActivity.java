package com.scu.lly.customviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.view.newstudyer.ChoutiMenuActivity;
import com.scu.lly.customviews.view.newstudyer.ClearAnim360Activity;
import com.scu.lly.customviews.view.newstudyer.CouponTestActivity;
import com.scu.lly.customviews.view.newstudyer.ErrorViewActivity;
import com.scu.lly.customviews.view.newstudyer.LoadingViewActivity;
import com.scu.lly.customviews.view.newstudyer.MaterialLoadingViewActivity;
import com.scu.lly.customviews.view.newstudyer.OverScrollByDemoActivity;
import com.scu.lly.customviews.view.newstudyer.PaopaoPopActivity;
import com.scu.lly.customviews.view.newstudyer.ProgressAnimActivity;
import com.scu.lly.customviews.view.newstudyer.SearchLoadingViewActivity;
import com.scu.lly.customviews.view.newstudyer.SpeedPanelActivity;
import com.scu.lly.customviews.view.newstudyer.WeixinEyesActivity;

/**
 * 新手自定义View实践系列
 * Created by lusheep on 2017/4/8.
 */

public class NewStudyerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstudyer);
    }

    public void paopaoDemo(View view){
        Intent i = new Intent(this, PaopaoPopActivity.class);
        startActivity(i);
    }

    public void progressAnimDemo(View view){
        Intent i = new Intent(this, ProgressAnimActivity.class);
        startActivity(i);
    }

    public void bolangviewDemo(View view){
        Intent i = new Intent(this, CouponTestActivity.class);
        startActivity(i);
    }

    public void chouticaidanDemo(View view){
        Intent i = new Intent(this, ChoutiMenuActivity.class);
        startActivity(i);
    }

    public void speedPanelDemo(View view){
        Intent i = new Intent(this, SpeedPanelActivity.class);
        startActivity(i);
    }

    public void loadingViewDemo(View view){
        Intent i = new Intent(this, LoadingViewActivity.class);
        startActivity(i);
    }

    public void errorViewDemo(View view){
        Intent i = new Intent(this, ErrorViewActivity.class);
        startActivity(i);
    }

    public void clearAnim360Demo(View view){
        Intent i = new Intent(this, ClearAnim360Activity.class);
        startActivity(i);
    }

    public void weixinEyesDemo(View view){
        Intent i = new Intent(this, WeixinEyesActivity.class);
        startActivity(i);
    }

    public void overscrollByDemo(View view){
        Intent i = new Intent(this, OverScrollByDemoActivity.class);
        startActivity(i);
    }

    public void materialloadingviewDemo(View view){
        Intent i = new Intent(this, MaterialLoadingViewActivity.class);
        startActivity(i);
    }

    public void searchloadingviewDemo(View view){
        Intent i = new Intent(this, SearchLoadingViewActivity.class);
        startActivity(i);
    }
}
