package com.scu.lly.customviews.view.newstudyer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * Created by lusheep on 2017/4/13.
 */

public class LoadingView extends View {

    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;

    private int mRadius;
    private int h1;//中间竖线高度（一半高度）
    private int h2;//中间等腰三角形高度
    private int fixedH2;//画三角形时，两边点的固定坐标
    private int dy;//中心圆点向顶部移动的距离
    private int sweepAngle;//外层白色圆扫过的角度

    private int dev;//三角形右边点的偏移

    private boolean isPointAnimStart;//判断中心圆点向顶部的动画过程是否开始

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(12);

        mPath = new Path();

        mRadius = (int) CommonUtils.dp2px(getContext(), 60);
        h1 = (int) CommonUtils.dp2px(getContext(), 20);
        h2 = (int) CommonUtils.dp2px(getContext(), 30);
        fixedH2 = h2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = (int) CommonUtils.dp2px(getContext(),100);
        }
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = (int) CommonUtils.dp2px(getContext(), 100);
        }

        mWidth = widthSize;
        mHeight = heightSize;

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先画外圈圆
        drawCircle(canvas);

        //画勾(先是等腰三角形，边长为h2)
        drawTriangle(canvas);

        //画中间竖线(上半高度好h1)
        drawLine(canvas);

    }

    private void drawLine(Canvas canvas) {
        mPaint.setColor(Color.WHITE);

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        if(h1 > 0) {//如果h1>0，就画线
            canvas.drawLine(0, h1, 0, -h1, mPaint);
        }else{//当h1<=0时，画一个点
            if(isPointAnimStart){
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(0, dy, 10, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);
            }
        }

        canvas.restore();
    }

    private void drawTriangle(Canvas canvas) {
        mPath.reset();
        mPaint.setPathEffect(new CornerPathEffect(3));
        mPaint.setColor(Color.WHITE);

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        mPath.moveTo(-fixedH2, 0);
        mPath.lineTo(0, h2);
        mPath.lineTo(fixedH2 + dev, -dev);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();
    }

    private void drawCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        //先画背景圆
        mPaint.setColor(Color.parseColor("#32A4F4"));
        canvas.drawCircle(0, 0, mRadius, mPaint);

        if(sweepAngle != 0){
            //再画白色圆弧
            mPaint.setColor(Color.WHITE);
            canvas.drawArc(new RectF(-mRadius, -mRadius, mRadius, mRadius), -90, sweepAngle, false, mPaint);
        }

        canvas.restore();
    }

    public void startAnim(){
        isPointAnimStart = true;
        ValueAnimator lineAnim = ValueAnimator.ofInt(h1, 0);
        lineAnim.setDuration(500);
        lineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                h1 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        lineAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //开始第二个动画（勾的动画）
                startPathLineAnim();
            }
        });
        lineAnim.start();
    }

    private void startPathLineAnim() {
        ValueAnimator anim = ValueAnimator.ofInt(h2, 0);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                h2 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //开始第三个动画（中心原点向顶部移动）
                startPoint();
            }
        });
        anim.start();
    }

    private void startPoint() {
        ValueAnimator anim = ValueAnimator.ofInt(0, 5 - mRadius);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dy = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //圆点动画结束后，不再画圆点
                isPointAnimStart = false;
                //开始最后一动画（画外层白色圆及勾动画）
                startOutWhiteCircle();
            }
        });
        anim.start();
    }

    private void startOutWhiteCircle() {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator anim = ValueAnimator.ofInt(0, -360);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        h2 = fixedH2;
        ValueAnimator anim2 = ValueAnimator.ofInt(0, h2);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                h2 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator anim3 = ValueAnimator.ofInt(0, 3 * fixedH2 / 5);
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dev = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        set.setDuration(500);
        set.playTogether(anim, anim2, anim3);
        set.start();
    }

}
