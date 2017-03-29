package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 仿手势书写View(使用 lineTo 和 二阶贝塞尔曲线quadTo 两种方式实现)
 * Created by lusheep on 2017/3/28.
 */

public class GestrueFakeView extends View {

    private Path mPath;
    private Paint mPaint;

    private int mLastX;
    private int mLastY;

    public GestrueFakeView(Context context) {
        super(context);
        init();
    }

    public GestrueFakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestrueFakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        /**
         * 方式一：使用lineTo方式实现

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                invalidate();
                break;
        }
         */

        /**
         * 使用二阶贝塞尔曲线quadTo的方式实现
         */
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                int endX = (mLastX + x) / 2;
                int endY = (mLastY + y) / 2;
                mPath.quadTo(mLastX, mLastY, endX, endY);
                mLastX = x;
                mLastY = y;
                invalidate();
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    public void reset(){
        mPath.reset();
        invalidate();
    }
}
