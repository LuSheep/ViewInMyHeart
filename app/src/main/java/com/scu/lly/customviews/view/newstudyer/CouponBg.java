package com.scu.lly.customviews.view.newstudyer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 优惠券背景效果
 * 这个只是练习，实际使用中推荐本项目中的另一个View：CouponBgView
 * Created by lusheep on 2017/4/10.
 */

public class CouponBg extends LinearLayout {

    private Paint mPaint;
    private Path mPath;
    private int mGapSize = 8;
    private int mRadius = 10;

    private int mWidth;
    private int mHeight;

    private int offset;

    private int count;

    public CouponBg(Context context) {
        super(context);
        init();
    }

    public CouponBg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CouponBg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        drawCircleHorizontal(canvas);
    }

    private void drawCircleHorizontal(Canvas canvas) {
        calculateHorizontal(mGapSize);

        int x = offset + mGapSize + mRadius;
        for(int i = 0; i < count; i ++){
            canvas.drawCircle(x, 0, mRadius, mPaint);
            x += 2 * mRadius + mGapSize;
        }

        x = offset + mGapSize + mRadius;
        for(int i = 0; i < count; i ++){
            canvas.drawCircle(x, mHeight, mRadius, mPaint);
            x += 2 * mRadius + mGapSize;
        }
    }

    private void calculateHorizontal(int gapSize) {
        count = (mWidth - gapSize) / (2 * mRadius + gapSize);
        offset = (mWidth - 2 * mRadius * count - (count + 1) * gapSize) / 2;
    }

    private void calculateVertical(int gapSize) {
        count = (mHeight - gapSize) / (2 * mRadius + gapSize);
        offset = (mHeight - 2 * mRadius * count - (count + 1) * gapSize) / 2;
    }


}
