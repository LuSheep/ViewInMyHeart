package com.scu.lly.customviews.view.newstudyer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * Created by lusheep on 2017/4/20.
 */

public class SearchLoadingView extends View {

    private Paint mPaint;

    private Path archPath;
    private Path linePath;
    private Path mPath;

    private int dg;//画里面的弧时逐渐减少的角度
    private int startPointLength;
    private int endPointLength;
    private int devLength;//线的初始点增长的距离

    private float startAngle;
    private float sweepAngle;

    private int mWidth;
    private int mHeight;

    private int mRadius;

    private boolean isOuterArchAnimStart;
    private boolean isStopTheAnim;//供外部调用

    public SearchLoadingView(Context context) {
        super(context);
        init();
    }

    public SearchLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        archPath = new Path();
        linePath = new Path();
        mPath = new Path();

        mRadius = 50;
        startPointLength = (int) (mRadius * Math.sin(Math.toRadians(45)));
        endPointLength = (int) (2 * mRadius * Math.sin(Math.toRadians(45)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = (int) CommonUtils.dp2px(getContext(),200);
        }
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = (int) CommonUtils.dp2px(getContext(), 200);
        }
        mWidth = widthSize;
        mHeight = heightSize;

        setMeasuredDimension(mWidth, mHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!isOuterArchAnimStart){
            drawPath(canvas);
        }else{
            canvas.save();
            canvas.translate(mWidth / 2, mHeight / 2);
            canvas.drawArc(-2 * mRadius, - 2 * mRadius, 2 * mRadius, 2 * mRadius, startAngle, sweepAngle, false, mPaint);
            canvas.restore();
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawPath(Canvas canvas){
        //画里面的圆弧
        archPath.reset();
        archPath.addArc(-mRadius, - mRadius, mRadius, mRadius, 45, dg - 360);

        //画搜索按钮的线
        linePath.reset();
        linePath.moveTo(startPointLength + devLength, startPointLength + devLength);
        linePath.lineTo(endPointLength, endPointLength);

        mPath.reset();
        mPath.addPath(archPath);
        mPath.addPath(linePath);

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        canvas.drawPath(mPath, mPaint);

        canvas.restore();
    }

    public void startAnim(){
        //先开始里面圆弧的动画
        ValueAnimator anim = ValueAnimator.ofInt(0, 360);
        anim.setDuration(800);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dg = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //开启线的动画
                startLineAnim();
            }
        });
        anim.start();
    }

    private void startLineAnim() {
        ValueAnimator anim = ValueAnimator.ofInt(0, startPointLength - 2);//留一点，不完全消失
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                devLength = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //开始外圈圆弧旋转动画
                startOuterArcAnim();
            }
        });
        anim.start();
    }

    private void startOuterArcAnim() {
        isOuterArchAnimStart = true;
        new Thread(){
            @Override
            public void run() {
                super.run();
                handler.post(r);
            }
        }.start();

    }

    private Handler handler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            final AnimatorSet set = new AnimatorSet();
            ValueAnimator anim = ValueAnimator.ofFloat(45, -315);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    startAngle = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            final ValueAnimator anim2 = ValueAnimator.ofFloat(1, 40, 1);
            anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    sweepAngle = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            set.setDuration(1200);
            set.playTogether(anim, anim2);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(!isStopTheAnim){
                        handler.post(r);
                    }else{
                        //复原搜索按钮
                        reStartSearchBtn();
                    }
                }
            });
            set.start();

        }
    };

    private void reStartSearchBtn() {
        isOuterArchAnimStart = false;

        ValueAnimator anim = ValueAnimator.ofInt(startPointLength, 0);
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                devLength = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //开始复原中间圆弧旋转动画
                startResartArcAnim();
            }
        });
        anim.start();
    }

    private void startResartArcAnim() {
        ValueAnimator anim = ValueAnimator.ofInt(360, 0);
        anim.setDuration(800);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dg = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isStopTheAnim = false;
            }
        });
        anim.start();
    }

    public void stopAnim(){
        isStopTheAnim = true;
    }
}
