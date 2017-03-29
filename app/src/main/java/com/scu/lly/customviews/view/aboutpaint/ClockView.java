package com.scu.lly.customviews.view.aboutpaint;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * 绘制时钟View
 * Created by lusheep on 2017/3/27.
 */

public class ClockView extends View {

    private Paint mPaint;

    private int mRadius = 150;//绘制圆的半径
    private int mStrokeWidth = 2;

    private Calendar cal;
    private float hourAngle,minAngle,secAngle;

    private boolean isRunning = false;

    private String[] clockNumbers = {"12","1","2","3","4","5","6","7","8","9","10","11"};

    private Handler mHandler;
    private Runnable mRunnable;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                postInvalidate();
                mHandler.postDelayed(this, 1000);
            }
        };
    }

    private void initPaint(){
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!isRunning){
            runClock();
        }else{
            drawClockDot(canvas);
            drawCircle(canvas);
            drawTick(canvas);
            drawCircleNumber(canvas);
            drawClockPoint(canvas);
        }
    }

    private void runClock() {
        isRunning = true;
        mHandler.postDelayed(mRunnable, 1000);
    }

    /**
     * 画三个指针
     * @param canvas
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void drawClockPoint(Canvas canvas) {
        cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        //计算时分秒指针各自需要偏移的角度
        hourAngle = (float)hour / 12 * 360 + (float)min / 60 * (360 / 12);//360/12是指每个数字之间的角度
        minAngle = (float)min / 60 * 360;
        secAngle = (float)seconds / 60 * 360;

        Log.d("TAG", "hour："+ hour + ", min：" + min + ", seconds：" + seconds);
        Log.d("TAG", "hourAngle："+ hourAngle + ", minAngle：" + minAngle + ", secAngle：" + secAngle);

        canvas.save();
        canvas.rotate(hourAngle,getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 50, mPaint);//时针长度设置为65
        canvas.restore();

        canvas.save();
        canvas.rotate(minAngle,getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 85 , mPaint);//分针长度设置为90
        canvas.restore();

        canvas.save();
        canvas.rotate(secAngle,getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 110 , mPaint);//秒针长度设置为110
        canvas.restore();

    }

    /**
     * 画圆上的数字
     * @param canvas
     */
    private void drawCircleNumber(Canvas canvas) {
        float baseX = 0;
        float baseY = 0;
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPaint.setTextSize(25);
        Rect textBounds = new Rect();
        for(int i = 0; i < clockNumbers.length; i++){
            float translateY = mRadius + 30;
            mPaint.getTextBounds(clockNumbers[i], 0, clockNumbers[i].length(), textBounds);

            baseX = -(textBounds.width() / 2);
            baseY = textBounds.height() / 2;

            canvas.save();
            canvas.translate(0, -translateY);
            canvas.rotate(-i * 30);
            canvas.drawText(clockNumbers[i], baseX, baseY, mPaint);
            canvas.restore();
            canvas.rotate(30);
        }
        canvas.restore();
    }

    private void drawTick2(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        int startX = 0;
        int startY = -mRadius;
        int endX = startX;
        int endY1 = 30 - mRadius;//大刻度
        int endY2 = 15 - mRadius;
        for(int i = 0; i < 60; i++){
            if(i % 5 == 0){
                canvas.drawLine(startX, startY, endX, endY1, mPaint);
            }else{
                canvas.drawLine(startX, startY, endX, endY2, mPaint);
            }
            canvas.rotate(6);
        }

        canvas.restore();
    }

    /**
     * 画刻度线
     * @param canvas
     */
    private void drawTick(Canvas canvas) {
        canvas.save();
        int startX = getWidth() / 2;
        int startY = getHeight() / 2 - mRadius;
        int endX = startX;
        int endY1 = startY + 30;//大刻度
        int endY2 = startY + 15;
        for(int i = 0; i < 60; i++){
            if(i % 5 == 0){
                canvas.drawLine(startX, startY, endX, endY1, mPaint);
            }else{
                canvas.drawLine(startX, startY, endX, endY2, mPaint);
            }
            canvas.rotate(6, getWidth() / 2, getHeight() / 2);
        }

        canvas.restore();
    }

    /**
     * 画外圈大圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
    }

    /**
     * 绘制中心圆点
     * @param canvas
     */
    private void drawClockDot(Canvas canvas) {
        mPaint.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 10, mPaint);
        mPaint.reset();
        initPaint();
    }
}
