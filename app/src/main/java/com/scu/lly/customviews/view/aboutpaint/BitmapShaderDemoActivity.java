package com.scu.lly.customviews.view.aboutpaint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.view.tagflow.TagFlowEnhanceView;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于BitmapShader的一些应用
 * Created by lusheep on 2017/4/4.
 */

public class BitmapShaderDemoActivity extends Activity {

    private TagFlowEnhanceView btns;
    List<String> tags = new ArrayList<String>();

    private TelescopeView mTelescopeView;
    private AvatorView mAvatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmapshaderdemo);

        initView();

        btns = (TagFlowEnhanceView) findViewById(R.id.bitmapshaderBtns);
        btns.setOnTagItemClickListener(new TagFlowEnhanceView.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                String text = tv.getText().toString();
                if(text.equals("望远镜效果(TileMode模式与绘制位置的关系)")){
                    mTelescopeView.setVisibility(View.VISIBLE);

                    mAvatorView.setVisibility(View.GONE);
                }else if(text.equals("圆角图片")){
                    mAvatorView.setVisibility(View.VISIBLE);

                    mTelescopeView.setVisibility(View.GONE);
                }
            }
        });

        initData();
        btns.setTags(tags);
    }

    private void initView() {
        mTelescopeView = (TelescopeView) findViewById(R.id.bitmapshader_telescopeview);
        mAvatorView = (AvatorView) findViewById(R.id.bitmapshader_avator);
    }

    private void initData() {
        tags.add("望远镜效果(TileMode模式与绘制位置的关系)");
        tags.add("圆角图片");
    }
}
