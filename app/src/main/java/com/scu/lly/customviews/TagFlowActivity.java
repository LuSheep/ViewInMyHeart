package com.scu.lly.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scu.lly.customviews.view.tagflow.TagFlowEnhanceView;
import com.scu.lly.customviews.view.tagflow.TagFlowView;

import java.util.ArrayList;
import java.util.List;

/**
 * 浮动标签
 * Created by lusheep on 2017/3/23.
 */

public class TagFlowActivity extends Activity {

    private TagFlowEnhanceView tagFlowView;

    List<String> tags = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagflow);

        tagFlowView = (TagFlowEnhanceView) findViewById(R.id.tag_flow);
        tagFlowView.setOnTagItemClickListener(new TagFlowEnhanceView.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                Toast.makeText(TagFlowActivity.this, tv.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        initData();
        tagFlowView.setTags(tags);
    }

    private void initData() {
        tags.add("阳哥你好！");
        tags.add("Android开发");
        tags.add("新闻热点");
        tags.add("热水进宿舍啦！");
        tags.add("I love you");
        tags.add("成都妹子");
        tags.add("新余妹子sdad");
        tags.add("仙女湖55");
        tags.add("创新工厂");
        tags.add("孵化园ooo");
        tags.add("神州100发射");
        tags.add("有毒疫苗");
        tags.add("顶你阳哥阳哥");
        tags.add("Hello World");
        tags.add("闲逛的蚂蚁孵化园v");
        tags.add("闲逛的蚂蚁孵化园孵化园孵化园孵化园");
        tags.add("闲逛的蚂蚁孵化园");
        tags.add("闲逛的蚂蚁神州100发射");
        tags.add("闲逛的蚂蚁2355465");
        tags.add("闲逛的蚂蚁");
    }
}
