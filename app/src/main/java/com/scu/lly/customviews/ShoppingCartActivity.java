package com.scu.lly.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scu.lly.customviews.adapter.GoodsAdapter;
import com.scu.lly.customviews.model.GoodsModel;
import com.scu.lly.customviews.utils.CartAddListener;
import com.scu.lly.customviews.utils.CartAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车添加动画
 * Created by lusheep on 2016/7/4.
 */
public class ShoppingCartActivity extends Activity implements CartAddListener{
    private RelativeLayout rootLayout;//用于展示动画的布局
    private ListView mListView;
    private ImageView cartIv;//购物车图片
    private TextView goodsCountTv;

    private List<GoodsModel> mDatas = new ArrayList<GoodsModel>();
    private GoodsAdapter mAdapter ;

    private int[] rootLocation = new int[2];
    private int[] cartLocation = new int[2];

    private PathMeasure mPathMeasure;

    private int goodsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);

        initView();
        initData();
    }

    private void initData() {
        GoodsModel good1 = new GoodsModel("蛋糕",R.drawable.cake);
        mDatas.add(good1);

        GoodsModel good2 = new GoodsModel("咖啡",R.drawable.coffee);
        mDatas.add(good2);

        GoodsModel good3 = new GoodsModel("茶壶",R.drawable.kettle);
        mDatas.add(good3);

        GoodsModel good4 = new GoodsModel("牛奶",R.drawable.milk);
        mDatas.add(good4);

        GoodsModel good5 = new GoodsModel("手机",R.drawable.mobile);
        mDatas.add(good5);

        mAdapter = new GoodsAdapter(this,mDatas);
        mAdapter.setCartAddListener(this);
        mListView.setAdapter(mAdapter);
    }

    private void initView() {
        rootLayout = (RelativeLayout) findViewById(R.id.rl_root);
        mListView = (ListView) findViewById(R.id.lv_goods);
        cartIv = (ImageView) findViewById(R.id.iv_cart);
        goodsCountTv = (TextView) findViewById(R.id.tv_goodscount);

    }

    @Override
    public void addGoodsToCart(ImageView goodsIv) {
        rootLayout.getLocationOnScreen(rootLocation);
        cartIv.getLocationOnScreen(cartLocation);

        int[] goodsLocation = new int[2];
        goodsIv.getLocationOnScreen(goodsLocation);
        //需要被添加进购物车的商品相对于rootLayout的初始坐标
        int goodsStartX = goodsLocation[0] - rootLocation[0] + goodsIv.getWidth() / 2;
        int goodsStartY = goodsLocation[1] - rootLocation[1] + goodsIv.getHeight() / 2;

        //计算购物车的坐标
        int cartX = cartLocation[0] - rootLocation[0] + cartIv.getWidth() / 2 - 40;//这里-40是为了商品进入购物车时更中间位置，是因为图片原因
        int cartY = cartLocation[1] - rootLocation[1];
        //计算贝塞尔曲线的控制点坐标
        int controlX = (goodsStartX + cartX) / 2;   //初步估计，根据需要调整
        int controlY = goodsStartY;

        Path path = new Path();
        path.moveTo(goodsStartX, goodsStartY);
        path.quadTo(controlX, controlY, cartX, cartY);

        //构造出一个商品的副本
        final ImageView goods = new ImageView(this);
        goods.setImageDrawable(goodsIv.getDrawable());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(goodsIv.getWidth(),goodsIv.getHeight());

        rootLayout.addView(goods, lp);

        /*
         * 这里是用属性动画来实现的，用属性动画实现，那就得提前用PathMeasure测量好Path的长度;
         * 还是推荐使用下面的自定义动画来实现
        */
        mPathMeasure = new PathMeasure(path,false);
        final float[] curPos = new float[2];
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curValue = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(curValue, curPos, null);
                goods.setTranslationX(curPos[0]);
                goods.setTranslationY(curPos[1]);
            }
        });
        valueAnimator.start();

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                goodsCount++;
                goodsCountTv.setText(String.valueOf(goodsCount));
                rootLayout.removeView(goods);
            }
        });

        /* 这里是自定义动画的实现(推荐方式)
        CartAnimation animation = new CartAnimation(path);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goodsCount++;
                goodsCountTv.setText(String.valueOf(goodsCount));
                rootLayout.removeView(goods);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        goods.startAnimation(animation);
        */

        /* 不使用Path来实现，直接使用属性动画来实现，目前还需要优化坐标的计算，待完成...
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(goods,"translationX",goodsStartX,goodsStartY);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(goods,"translationY",cartX,cartY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.setDuration(1000);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                goodsCount++;
                goodsCountTv.setText(String.valueOf(goodsCount));
                rootLayout.removeView(goods);
            }
        });
         */
    }
}
