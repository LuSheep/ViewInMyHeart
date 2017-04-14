package com.scu.lly.customviews.view.newstudyer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;

import com.scu.lly.customviews.R;

/**
 * 有回弹效果的ListView
 * Created by lusheep on 2017/4/14.
 */

public class OverScrollByView extends ListView {

    private ImageView mHeadIv;

    private int mOrightHeight;//ImageView的初始高度
    private int mDrawableHieght;//ImageView中的图片高度

    private ValueAnimator anim;

    public OverScrollByView(Context context) {
        this(context, null);
    }

    public OverScrollByView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollByView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.header_overscrollview, null);
        mHeadIv = (ImageView) headView.findViewById(R.id.id_header_overscroll_iv);

        addHeaderView(headView);

        mHeadIv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mHeadIv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mOrightHeight = mHeadIv.getHeight();
                mDrawableHieght = mHeadIv.getDrawable().getIntrinsicHeight();
            }
        });

    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if(deltaY < 0 && mHeadIv.getHeight() <= mDrawableHieght){
            //这里除以2.0f是为了达到视差的效果
            mHeadIv.getLayoutParams().height = (int) (mHeadIv.getHeight() + Math.abs(deltaY / 2.0f));
            mHeadIv.requestLayout();
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            if(anim != null && anim.isRunning()){
                anim.cancel();
            }
        }
        if(ev.getAction() == MotionEvent.ACTION_UP){//回弹
            startBoundAnim();
        }


        return super.onTouchEvent(ev);
    }

    private void startBoundAnim() {
        anim = ValueAnimator.ofInt(mHeadIv.getHeight(), mOrightHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                mHeadIv.getLayoutParams().height = h;
                mHeadIv.requestLayout();
            }
        });
        anim.start();
    }
}
