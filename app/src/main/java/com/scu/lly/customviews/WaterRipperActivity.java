package com.scu.lly.customviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.scu.lly.customviews.view.water.WaterRipple;

/**
 * Created by lusheep on 2016/7/1.
 */
public class WaterRipperActivity extends Activity {

    private WaterRipple waterRipple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterripper);

        waterRipple = (WaterRipple) findViewById(R.id.id_waterRipple);
        waterRipple.setOnCompleteListener(new WaterRipple.OnCompleteAnimation() {
            @Override
            public void onComplete(int id) {
                if(id == R.id.id_water_btn){
                    Toast.makeText(WaterRipperActivity.this,"--->onComplete click",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(WaterRipperActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    /**
     * 设置了OnCompleteAnimation监听就不要再设置控件的点击事件了，否则会有两次点击事件
     * @param v
     */
    public void btnClick(View v){
//        Toast.makeText(this,"--->click",Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(this,MainActivity.class);
//        startActivity(i);
    }

    public void tvClick(View v){
        Toast.makeText(this,"--->tvClick",Toast.LENGTH_SHORT).show();
    }
}
