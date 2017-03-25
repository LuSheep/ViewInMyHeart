package com.scu.lly.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.scu.lly.customviews.adapter.SlidingBaseAdapter;
import com.scu.lly.customviews.model.SlidingItemModel;
import com.scu.lly.customviews.view.slidingview.SlidingListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lusheep on 2016/7/1.
 */
public class SlidingActivity extends Activity{
    private SlidingListView listView;
    private List<SlidingItemModel> mDatas = new ArrayList<SlidingItemModel>();
    private SlidingBaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);

        initData();
        initView();
    }

    private void initView() {
        listView = (SlidingListView) findViewById(R.id.id_lv);
        mAdapter = new SlidingBaseAdapter(this,mDatas);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((SlidingItemModel)parent.getAdapter().getItem(position)).name;
                Toast.makeText(SlidingActivity.this, "--->" + position + "-->" + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        for(int i =0; i < 20; i++){
            SlidingItemModel item = new SlidingItemModel();
            item.name = "hello world" + i;
            mDatas.add(item);
        }
    }
}
