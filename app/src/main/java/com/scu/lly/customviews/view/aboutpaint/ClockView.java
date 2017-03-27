package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 绘制时钟View
 * Created by lusheep on 2017/3/27.
 */

public class ClockView extends View {

    private Paint mPaint;

    private int mRadius = 150;//绘制圆的半径
    private int mStrokeWidth = 2;

    public ClockView(Context context) {
        super(context);
        initPaint();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawClockDot(canvas);
    }

    /**
     * 绘制中心圆点
     * @param canvas
     */
    private void drawClockDot(Canvas canvas) {

    }
}
