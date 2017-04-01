package com.scu.lly.customviews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.adapter.SlidingBaseAdapter;
import com.scu.lly.customviews.model.SlidingItemModel;
import com.scu.lly.customviews.view.slidingview.SlidingListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlidingListView listView;
    private List<SlidingItemModel> mDatas = new ArrayList<SlidingItemModel>();
    private SlidingBaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void slidingDemo(View v){
        Intent i = new Intent(this,SlidingActivity.class);
        startActivity(i);
    }

    public void waterDemo(View v){
        Intent i = new Intent(this,WaterRipperActivity.class);
        startActivity(i);
    }

    public void couponDemo(View v){
        Intent i = new Intent(this,CouponActivity.class);
        startActivity(i);
    }

    public void shoppingDemo(View v){
        Intent i = new Intent(this,ShoppingCartActivity.class);
        startActivity(i);
    }

    public void qqHealthDemo(View v){
        Intent i = new Intent(this,QQHealthActivity.class);
        startActivity(i);
    }

    public void customImageViewDemo(View v){
        Intent i = new Intent(this,CustomImageViewActivity.class);
        startActivity(i);
    }

    public void imageLoaderDemo(View view){
        Intent i = new Intent(this,ImageLoaderTestActivity.class);
        startActivity(i);
    }

    public void pinnedHeaderViewDemo(View view){
        Intent i = new Intent(this,PinnedHeaderExpandabeListViewActivity.class);
        startActivity(i);
    }

    public void tagFlowDemo(View view){
        Intent i = new Intent(this,TagFlowActivity.class);
        startActivity(i);
    }

    public void paintDemo(View view){
        Intent i = new Intent(this,PaintDemoActivity.class);
        startActivity(i);
    }

    public void animDemo(View view){
        Intent i = new Intent(this,AnimDemoActivity.class);
        startActivity(i);
    }

    public void redpointDemo(View view){
        Intent i = new Intent(this,QQRedPointViewActivity.class);
        startActivity(i);
    }

}
