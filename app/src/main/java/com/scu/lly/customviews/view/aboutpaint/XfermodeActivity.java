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
 * Created by lusheep on 2017/3/31.
 */

public class XfermodeActivity extends Activity {

    private TagFlowEnhanceView btns;
    List<String> tags = new ArrayList<String>();

    //下面是功能View
    private XfermodeCircleImageView xfermodeCircleImageView;
    private XfermodeInvertedView xfermodeInvertedView;
    private EraserView eraserView;
    private GuaGuaKaView guaguaKaView;
    private CircleWaveView_DSTIN circleWaveView;
    private HeartMapView heartMapView;
    private IrregularWaveView irregularWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode);

        initView();

        btns = (TagFlowEnhanceView) findViewById(R.id.xfermodeBtns);
        btns.setOnTagItemClickListener(new TagFlowEnhanceView.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                String text = tv.getText().toString();
                if("XfermodeCircleImageView".equals(text)){
                    xfermodeCircleImageView.setVisibility(View.VISIBLE);

                    xfermodeInvertedView.setVisibility(View.GONE);
                    eraserView.setVisibility(View.GONE);
                    guaguaKaView.setVisibility(View.GONE);
                    circleWaveView.setVisibility(View.GONE);
                    heartMapView.setVisibility(View.GONE);
                    irregularWaveView.setVisibility(View.GONE);
                }else if("Xfermode倒影效果".equals(text)){
                    xfermodeInvertedView.setVisibility(View.VISIBLE);

                    xfermodeCircleImageView.setVisibility(View.GONE);
                    eraserView.setVisibility(View.GONE);
                    guaguaKaView.setVisibility(View.GONE);
                    circleWaveView.setVisibility(View.GONE);
                    heartMapView.setVisibility(View.GONE);
                    irregularWaveView.setVisibility(View.GONE);
                }else if("橡皮擦效果".equals(text)){
                    eraserView.setVisibility(View.VISIBLE);

                    xfermodeInvertedView.setVisibility(View.GONE);
                    xfermodeCircleImageView.setVisibility(View.GONE);
                    guaguaKaView.setVisibility(View.GONE);
                    circleWaveView.setVisibility(View.GONE);
                    heartMapView.setVisibility(View.GONE);
                    irregularWaveView.setVisibility(View.GONE);
                }else if("刮刮卡".equals(text)){
                    guaguaKaView.setVisibility(View.VISIBLE);

                    eraserView.setVisibility(View.GONE);
                    xfermodeInvertedView.setVisibility(View.GONE);
                    xfermodeCircleImageView.setVisibility(View.GONE);
                    circleWaveView.setVisibility(View.GONE);
                    heartMapView.setVisibility(View.GONE);
                    irregularWaveView.setVisibility(View.GONE);
                }else if("区域波纹".equals(text)){
                    circleWaveView.setVisibility(View.VISIBLE);

                    guaguaKaView.setVisibility(View.GONE);
                    eraserView.setVisibility(View.GONE);
                    xfermodeInvertedView.setVisibility(View.GONE);
                    xfermodeCircleImageView.setVisibility(View.GONE);
                    heartMapView.setVisibility(View.GONE);
                    irregularWaveView.setVisibility(View.GONE);
                }else if("心电图".equals(text)){
                    heartMapView.setVisibility(View.VISIBLE);

                    circleWaveView.setVisibility(View.GONE);
                    guaguaKaView.setVisibility(View.GONE);
                    eraserView.setVisibility(View.GONE);
                    xfermodeInvertedView.setVisibility(View.GONE);
                    xfermodeCircleImageView.setVisibility(View.GONE);
                    irregularWaveView.setVisibility(View.GONE);
                }else if("不规则波纹".equals(text)){
                    irregularWaveView.setVisibility(View.VISIBLE);

                    heartMapView.setVisibility(View.GONE);
                    circleWaveView.setVisibility(View.GONE);
                    guaguaKaView.setVisibility(View.GONE);
                    eraserView.setVisibility(View.GONE);
                    xfermodeInvertedView.setVisibility(View.GONE);
                    xfermodeCircleImageView.setVisibility(View.GONE);
                }
            }
        });

        initData();
        btns.setTags(tags);
    }

    private void initView() {
        xfermodeCircleImageView = (XfermodeCircleImageView) findViewById(R.id.xfermode_circleview);
        xfermodeInvertedView = (XfermodeInvertedView) findViewById(R.id.xfermode_invertview);
        eraserView = (EraserView) findViewById(R.id.xfermode_eraseview);
        guaguaKaView = (GuaGuaKaView) findViewById(R.id.xfermode_guaguakaview);
        circleWaveView = (CircleWaveView_DSTIN) findViewById(R.id.xfermode_circlrwave);
        heartMapView = (HeartMapView) findViewById(R.id.xfermode_heartmapview);
        irregularWaveView = (IrregularWaveView) findViewById(R.id.xfermode_irregularview);

    }

    private void initData() {
        tags.add("XfermodeCircleImageView");
        tags.add("Xfermode倒影效果");
        tags.add("橡皮擦效果");
        tags.add("刮刮卡");
        tags.add("区域波纹");
        tags.add("心电图");
        tags.add("不规则波纹");
    }
}
