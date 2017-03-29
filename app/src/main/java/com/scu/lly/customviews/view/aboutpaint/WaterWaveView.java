package com.scu.lly.customviews.view.aboutpaint;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 水波纹效果
 * Created by lusheep on 2017/3/29.
 */

public class WaterWaveView extends View {

    private Paint mPaint;
    private Path mPath;

    /**
     * 将波长设置为400等小于getWidth()的长度时，屏幕中会有多个波长存在，动画效果起来后，看起来更像是移动效果而不是水波纹效果；
     * 将波长设置较大时，如下的1000，一个平面中不可能存在多个波长，一般是半个或小于半个波长，这样动起来之后更像是水波纹效果。
     */
    private int mItemWaveLength = 1000;//一个波长的长度
    private int mItemHalfWaveLength;//半个波长的距离
    private int dx;//动画过程中X轴移动的距离
    private int dy;//动画过程中Y轴移动的距离

    private int mWaveY = 500;//波浪在Y轴的位置

    private boolean isRunning = false;

    public WaterWaveView(Context context) {
        super(context);
        init();
    }

    public WaterWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mItemHalfWaveLength = mItemWaveLength / 2;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(!isRunning){
            isRunning = true;
            Log.d("TAG", "getHeight():---"+ getHeight());
            startAnim();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        if(dy > getHeight() * 4 / 5){//y轴向下的动画重复进行
            dy = 0;
        }
        mPath.moveTo(-mItemWaveLength + dx, mWaveY + dy);
        dy += 1;

        for(int i = -mItemWaveLength; i <= getWidth() + mItemWaveLength; i += mItemWaveLength){
            //绘制前半个波长
            mPath.rQuadTo(mItemHalfWaveLength / 2, -100, mItemHalfWaveLength, 0);//控制点移动1/4个波长
            //绘制后半个波长
            mPath.rQuadTo(mItemHalfWaveLength / 2, 100, mItemHalfWaveLength, 0);
        }

        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        canvas.drawPath(mPath, mPaint);

    }

    public void startAnim(){
        //x轴动起来
        ValueAnimator anim = ValueAnimator.ofInt(0, mItemWaveLength);
        anim.setDuration(2000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.start();
    }
}
