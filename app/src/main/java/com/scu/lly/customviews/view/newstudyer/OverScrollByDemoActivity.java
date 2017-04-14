package com.scu.lly.customviews.view.newstudyer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/4/14.
 */

public class OverScrollByDemoActivity extends Activity {
    private String[] NAMES = {"lly1", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6", "lly2", "lly3", "lly4", "lly5", "lly6"};

    private OverScrollByView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overscrollby);

        mListView = (OverScrollByView) findViewById(R.id.id_overscrollview);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NAMES));
    }
}
