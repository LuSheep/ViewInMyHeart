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
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * Created by lusheep on 2017/4/14.
 */

public class MaterialLoadingView extends View {

    private Paint mPaint;
    //两个点运动的路径
    private Path mPath1;
    private Path mPath2;
    //两个点之间的曲线路径
    private Path mPath3;

    private PathMeasure mPathMeasure1;
    private PathMeasure mPathMeasure2;

    private int mRadius = 20;//两个圆的半径
    private int r = 2 * mRadius;//绕行路径圆的半径

    private int mWidth;
    private int mHeight;

    private float x1, y1;//圆1的绘制中心坐标
    private float x2, y2;//圆2的绘制中心坐标
    private float distance;//两个圆中心点之间的距离

    private boolean isAnimStart;

    private PointF p0 = new PointF();//控制点坐标
    private PointF p1 = new PointF();
    private PointF p2 = new PointF();
    private PointF p3 = new PointF();
    private PointF p4 = new PointF();

    public MaterialLoadingView(Context context) {
        super(context);
        init();
    }

    public MaterialLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
//        mPaint.setStyle(Paint.Style.STROKE);

        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();

        mPathMeasure1 = new PathMeasure();
        mPathMeasure2 = new PathMeasure();
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

        x1 = x2 = mWidth / 2;
        y1 = y2 = mHeight / 2;

        mPath1.reset();
        mPath1.moveTo(mWidth / 2, mHeight / 2);
        mPath1.lineTo(mWidth / 2, mHeight / 2 - 3 * mRadius / 2);
        mPath1.quadTo(mWidth / 2 + 3 * mRadius, mHeight / 2, mWidth / 2, mHeight / 2 + 3 * mRadius / 2);
        mPath1.lineTo(mWidth / 2, mHeight / 2);

        mPath2.reset();
        mPath2.moveTo(mWidth / 2, mHeight / 2);
        mPath2.lineTo(mWidth / 2, mHeight / 2 + 3 * mRadius / 2);
        mPath2.quadTo(mWidth / 2 - 3 * mRadius, mHeight / 2, mWidth / 2, mHeight / 2 - 3 * mRadius / 2);
        mPath2.lineTo(mWidth / 2, mHeight / 2);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画出两个圆的运动轨迹（仅测试效果时使用）
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setPathEffect(new CornerPathEffect(5));
//        canvas.drawPath(mPath1, mPaint);
//        canvas.drawPath(mPath2, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x1, y1, mRadius, mPaint);
        canvas.drawCircle(x2, y2, mRadius, mPaint);

        //判断两中心圆点之间的距离，如果距离<= mRadius/2 ，则画两点之间的连线（形成胶着效果）
        distance = getDistance(x1, y1, x2, y2);
        if(isAnimStart && distance <= 5 * mRadius / 2){
            calculatePoints(x1, y1, x2, y2);
            mPath3.reset();
            mPaint.setStyle(Paint.Style.FILL);

            mPath3.moveTo(p1.x, p1.y);
            mPath3.quadTo(p0.x, p0.y, p2.x, p2.y);
            mPath3.lineTo(p3.x, p3.y);
            mPath3.quadTo(p0.x, p0.y, p4.x, p4.y);
            mPath3.close();

            canvas.drawPath(mPath3, mPaint);
        }
    }

    /**
     * 计算控制点和四个切点坐标（此处类似qq未读消息view）
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private void calculatePoints(float x1, float y1, float x2, float y2) {
        p0.x = (x1 + x2) / 2;
        p0.y = (y1 + y2) / 2;

        float dx = x2 - x1;
        float dy = y2 - y1;

        double a = Math.atan(dy / dx);

        float offsetX = (float) (mRadius * Math.sin(a));
        float offsetY = (float) (mRadius * Math.cos(a));

        p1.x = x1 + offsetX;
        p1.y = y1 - offsetY;

        p2.x = x2 + offsetX;
        p2.y = y2 - offsetY;

        p3.x = x2 - offsetX;
        p3.y = y2 + offsetY;

        p4.x = x1 - offsetX;
        p4.y = y1 + offsetY;
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void startAnim(){
        mPathMeasure1.setPath(mPath1, false);
        mPathMeasure2.setPath(mPath2, false);

        isAnimStart = true;

        AnimatorSet set = new AnimatorSet();

        ValueAnimator anim = ValueAnimator.ofFloat(0, mPathMeasure1.getLength());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                float[] pos = new float[2];
                mPathMeasure1.getPosTan(distance, pos, null);
                x1 = pos[0];
                y1 = pos[1];
                postInvalidate();
            }
        });
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);

        ValueAnimator anim2 = ValueAnimator.ofFloat(0, mPathMeasure2.getLength());
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                float[] pos = new float[2];
                mPathMeasure2.getPosTan(distance, pos, null);
                x2 = pos[0];
                y2 = pos[1];
                postInvalidate();
            }
        });
        anim2.setRepeatMode(ValueAnimator.RESTART);
        anim2.setRepeatCount(ValueAnimator.INFINITE);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimStart = false;
            }
        });


        set.setDuration(2000);
        set.playTogether(anim, anim2);
        set.start();
    }
}
