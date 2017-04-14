package com.scu.lly.customviews.view.newstudyer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.utils.CommonUtils;

/**
 * Created by lusheep on 2017/4/13.
 */

public class ClearAnim360View extends View {

    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;

    private int lineY;//初始线y的坐标
    private int x1, x2;//两个点的x坐标

    private int pointX, pointY;//手指移动时的坐标（是指曲线上的一点）
    private int controllX, controllY;//控制点坐标（知道了两个端点和曲线上一点，根据二阶贝塞尔曲线公式可以求得控制点坐标）
    private float t = 0.5f; //二次贝赛尔曲线的参数t

    private int endAnimX, endAnimY;//动画发射线的终点坐标

    private int animX, animY;

    private Bitmap mBitmap;

    private boolean isTouch;
    private boolean isUp;
    private boolean isAnimStart;
    private boolean isResetAnimStart;

    public ClearAnim360View(Context context) {
        super(context);
        init();
    }

    public ClearAnim360View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearAnim360View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);

        mPath = new Path();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, 60, 60, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = (int) CommonUtils.dp2px(getContext(),300);
        }
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = (int) CommonUtils.dp2px(getContext(), 500);
        }

        mWidth = widthSize;
        mHeight = heightSize;
        lineY = 3 * mHeight / 4;
        x1 = 100;
        x2 = mWidth - 100;
//        controllX = (x1 + x2) / 2;
//        controllY = lineY;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先画两个点
        drawPoint(canvas);

        drawPathLine(canvas);
    }

    private void drawPathLine(Canvas canvas) {
        mPath.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        //画bitmap
        if(isTouch){
            //根据二阶贝塞尔曲线公式求得控制点坐标（与以往有点不同，此时是知道了控制点对应的曲线上的坐标，来求控制点坐标，只需要将公式变换一下即可）
            controllX = (int) ( (pointX - (1-t) * (1-t) * x1 - t * t * x2)/( 2 * t * ( 1-t )) );
            controllY = (int) ( (pointY - (1-t) * (1-t) * lineY- t * t * lineY)/( 2 * t * ( 1-t )) );

            mPath.moveTo(x1, lineY);
            mPath.quadTo(controllX, controllY , x2, lineY);
            canvas.drawPath(mPath, mPaint);

            if(!isAnimStart && !isResetAnimStart){
                canvas.drawBitmap(mBitmap, pointX - mBitmap.getWidth() / 2, pointY - mBitmap.getHeight() / 2, mPaint);
            }
        }else{
            mPath.moveTo(x1, lineY);
            mPath.lineTo(x2, lineY);
            canvas.drawPath(mPath, mPaint);
        }

        if(isAnimStart){
            canvas.drawBitmap(mBitmap, animX - mBitmap.getWidth() / 2, animY - mBitmap.getHeight() / 2, mPaint);
        }

    }

    private void drawPoint(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x1, lineY, 15, mPaint);
        canvas.drawCircle(x2, lineY, 15, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(Math.abs(y - lineY) > 20){
                    return false;
                }
                isTouch = true;
                pointX = x;
                pointY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                pointX = x;
                pointY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isUp = true;
//                isTouch = false;
                pointX = x;
                pointY = y;
                startAnim();
//                invalidate();
                break;
        }
        return true;
    }

    private void startAnim() {
        isAnimStart = true;
        int m = (x1 + x2) / 2;
        endAnimX = 2 * m - pointX;
        endAnimY = 2 * lineY - pointY;

        Path p = new Path();
        p.moveTo(pointX, pointY);
        p.lineTo(endAnimX, endAnimY);
        final PathMeasure pm = new PathMeasure(p, false);

        ValueAnimator anim = ValueAnimator.ofFloat(0, pm.getLength());
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();

                float[] temp = new float[2];
                pm.getPosTan(distance, temp, null);
                animX = (int) temp[0];
                animY = (int) temp[1];
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimStart = false;
                postInvalidate();
                resetLine();
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

    }

    private void resetLine() {
        isResetAnimStart = true;
        int m = (x1 + x2) / 2;
        ValueAnimator anim = ValueAnimator.ofInt(pointX, m);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointX = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        ValueAnimator anim2 = ValueAnimator.ofInt(pointY, lineY);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointY = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(anim, anim2);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isTouch = false;
                isResetAnimStart = false;
            }
        });
        set.start();
    }
}
