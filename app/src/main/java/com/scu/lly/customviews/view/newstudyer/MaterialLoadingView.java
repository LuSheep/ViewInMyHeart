package com.scu.lly.customviews.view.newstudyer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * Created by lusheep on 2017/4/14.
 */

public class MaterialLoadingView extends View {

    private Paint mPaint;

    private Path mPath1;
    private Path mPath2;

    private int mRadius = 20;//两个圆的半径
    private int r = 2 * mRadius;//绕行路径圆的半径

    private int mWidth;
    private int mHeight;

    private int x1, y1;//圆1的绘制中心坐标
    private int x2, y2;//圆2的绘制中心坐标

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
        mPath1.lineTo(mWidth / 2, mHeight / 2 - 2 * r);
        mPath1.addArc(new RectF(mWidth / 2 - 2 * r, mHeight / 2 - 2 * r, mWidth / 2 + 2 * r, mHeight / 2 + 2 * r),
                -90, 180);
        mPath1.lineTo(mWidth / 2, mHeight / 2);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath1, mPaint);
    }
}
