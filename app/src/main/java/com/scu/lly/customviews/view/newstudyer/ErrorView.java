package com.scu.lly.customviews.view.newstudyer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * Created by lusheep on 2017/4/13.
 */

public class ErrorView extends View {

    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private int mRadius;
    private int mStrokeWidth = 8;

    private int sweepAngle;
    private int mHalfLength;//一根线的一半长度
    private int dev1;
    private int dev2;

    private boolean isArcAnimStart;
    private boolean isLine1AnimStart;
    private boolean isLine2AnimStart;

    public ErrorView(Context context) {
        super(context);
        init();
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        startAnim();
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

        int w = Math.min(mWidth, mHeight);

        mWidth = w;
        mHeight = w;

        mRadius = w / 2 - mStrokeWidth / 2;

        mHalfLength = 3 * w / 5 / 2;//3/5长度w 的一半

        setMeasuredDimension(w, w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawArc(canvas);

        drawLine1(canvas);
        drawLine2(canvas);

    }

    private void drawLine1(Canvas canvas) {
        if(isLine1AnimStart){
            canvas.save();
            canvas.translate(mWidth / 2, mHeight / 2);
            canvas.rotate(45);

            canvas.drawLine(-mHalfLength, 0, dev1, 0, mPaint);

            canvas.restore();
        }
    }

    private void drawLine2(Canvas canvas) {
        if(isLine2AnimStart){
            canvas.save();
            canvas.translate(mWidth / 2, mHeight / 2);
            canvas.rotate(45);

            canvas.drawLine(0, mHalfLength, 0, dev2, mPaint);

            canvas.restore();
        }
    }

    private void drawArc(Canvas canvas) {
        if(isArcAnimStart && sweepAngle > 0){
            canvas.save();
            canvas.translate(mWidth / 2, mHeight / 2);

            canvas.drawArc(new RectF(-mRadius, -mRadius, mRadius, mRadius), 180,sweepAngle, false, mPaint);

            canvas.restore();
        }
    }

    private void startAnim(){
        isArcAnimStart = true;
        ValueAnimator anim = ValueAnimator.ofInt(-10, 360);//从负数开始，避免一开始就直接画圆效果
        anim.setDuration(700);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //画第一条线
                startDrawLine1();
            }
        });
        anim.start();
    }

    private void startDrawLine1() {
        isLine1AnimStart = true;
        ValueAnimator anim = ValueAnimator.ofInt(-mHalfLength, mHalfLength);
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dev1 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //画第二条线
                startDrawLine2();
            }
        });
        anim.start();
    }

    private void startDrawLine2() {
        isLine2AnimStart = true;
        ValueAnimator anim = ValueAnimator.ofInt(mHalfLength, -mHalfLength);
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dev2 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //最后一个动画（抖动效果）
                startLastAnim();
            }
        });
        anim.start();
    }

    private void startLastAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "translationX",
                0, mHalfLength/2, 0, -mHalfLength/2, 0, mHalfLength/2, 0, -mHalfLength/2, 0, mHalfLength/2, 0, -mHalfLength/2, 0);
        anim.setDuration(700);
        anim.setInterpolator(new LinearInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //标志位全部重置
                isArcAnimStart = false;
                isLine1AnimStart = false;
                isLine2AnimStart = false;
            }
        });
        anim.start();
    }
}
