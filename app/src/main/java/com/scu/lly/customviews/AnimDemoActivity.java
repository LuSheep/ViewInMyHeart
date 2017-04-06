package com.scu.lly.customviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scu.lly.customviews.view.anim.ListItemCustomAnimActivity;
import com.scu.lly.customviews.view.anim.ListViewItemAnimActivity;
import com.scu.lly.customviews.view.anim.MenuAnimActivity;
import com.scu.lly.customviews.view.anim.TestAnimActivity;

/**
 * Created by lusheep on 2017/3/30.
 */

public class AnimDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animdemo);
    }

    public void testAnimDemo(View view){
        Intent i = new Intent(this, TestAnimActivity.class);
        startActivity(i);
    }

    public void listViewAnimDemo(View view){
        Intent i = new Intent(this, ListViewItemAnimActivity.class);
        startActivity(i);
    }

    public void menuAnimDemo(View view){
        Intent i = new Intent(this, MenuAnimActivity.class);
        startActivity(i);
    }

    public void listCustomDemo(View view){
        Intent i = new Intent(this, ListItemCustomAnimActivity.class);
        startActivity(i);
    }
}
