package com.scu.lly.customviews.view.anim;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ListView;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.adapter.ListItemAnimAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView的item进入动画（自定义的动画效果，中途加入item照样可以有效果）
 * Created by lusheep on 2017/4/6.
 */

public class ListItemCustomAnimActivity extends Activity {

    private ListView mListView;
    private ListItemAnimAdapter mAdapter;
    private List<Drawable> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcustomanim);

        mListView = (ListView) findViewById(R.id.listview_customanim);

        initData();

        mAdapter = new ListItemAnimAdapter(list, this, 300, mListView);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        list.add(getResources().getDrawable(R.mipmap.pic1));
        list.add(getResources().getDrawable(R.mipmap.pic2));
        list.add(getResources().getDrawable(R.mipmap.pic3));
        list.add(getResources().getDrawable(R.mipmap.pic4));
        list.add(getResources().getDrawable(R.mipmap.pic5));
        list.add(getResources().getDrawable(R.mipmap.pic6));
        list.add(getResources().getDrawable(R.mipmap.pic7));
        list.add(getResources().getDrawable(R.mipmap.pic8));
        list.add(getResources().getDrawable(R.mipmap.pic9));
    }
}
