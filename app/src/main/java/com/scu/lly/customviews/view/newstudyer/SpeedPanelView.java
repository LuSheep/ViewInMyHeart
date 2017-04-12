package com.scu.lly.customviews.view.newstudyer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * 时速盘View
 * Created by lusheep on 2017/4/10.
 */

public class SpeedPanelView extends View {

    private Paint mPaint;

    private int mRadius1 = 20;//最中心白色圆半径
    private int mStrokeWidth1 = 12;

    private int mRadius2 = 40;
    private int mStrokeWidth2 = 6;

    private int mArcRaidus;
    private int mStrokeWidthArc = 60;

    private int mWidth;
    private int mHeight;

    private int progress;
    private int total = 100;//进度最大值

    private String text = "时速盘";

    public SpeedPanelView(Context context) {
        super(context);
        init();
    }

    public SpeedPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpeedPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
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
            heightSize = (int) CommonUtils.dp2px(getContext(), 220);
        }

        mWidth = widthSize;
        mHeight = heightSize;

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawCircle1(canvas);
        drawCircle2(canvas);

        drawArcPanel(canvas);

        drawScale(canvas);

        drawPoint(canvas);
        drawTagRectAndText(canvas);
    }

    private void drawTagRectAndText(Canvas canvas) {
        int left = -50;
        int top = mArcRaidus - 20;
        int right = 50;
        int bottom = mArcRaidus + 20;

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        mPaint.setColor(Color.parseColor("#5FB1ED"));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(left, top, right, bottom), mPaint);

        //绘制文字
        int centerY = mArcRaidus + 65;
        mPaint.setTextSize(30);
        mPaint.setColor(Color.WHITE);
        int x = (int) (-mPaint.measureText(text) / 2);
        canvas.drawText(text, x, centerY, mPaint);

        canvas.restore();

        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawPoint(Canvas canvas) {
        if(progress > total)
            progress = total;
        int r = mArcRaidus - mStrokeWidthArc / 2 + 20;

        canvas.save();

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidth2);

        float startX = (float) -(mRadius1 * Math.cos(Math.toRadians(30)));
        float startY = (float) (mRadius1 * Math.sin(Math.toRadians(30)));
        float stopX = (float) -(r * Math.cos(Math.toRadians(30)));
        float stopY = (float) (r * Math.sin(Math.toRadians(30)));
        float sweepAngle = progress * 1.0f / total * 240;//240为背景圆弧的总弧度大小

        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.rotate(sweepAngle);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        canvas.restore();
    }

    private void drawScale(Canvas canvas) {
        float r = mArcRaidus + mStrokeWidthArc / 2 + 5 * mStrokeWidthArc / 4;
        float startX = (float) -(r * Math.cos(Math.toRadians(30)));
        float startY = (float) (r * Math.sin(Math.toRadians(30)));

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint.setStrokeWidth(mStrokeWidth2);
        mPaint.setColor(Color.parseColor("#5FB1ED"));
        //先画圆弧
        canvas.drawArc(new RectF(-r, -r, r, r), 150, 240, false, mPaint);

        r = r - 30;
        float stopX = (float) -(r * Math.cos(Math.toRadians(30)));
        float stopY = (float) (r * Math.sin(Math.toRadians(30)));
        //再画刻度
        for(int i = 0; i < 13; i++){
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
            canvas.rotate(20);
        }

        canvas.restore();
    }

    /**
     * 画时速盘圆弧(整个弧使用了30 + 180 + 30 = 240弧度)
     * @param canvas
     */
    private void drawArcPanel(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidthArc);

        mArcRaidus = 1 * mWidth / 2 / 2 - mStrokeWidthArc / 2;//时速盘整个宽度占控件的1/2

        int size = (mArcRaidus + mStrokeWidthArc / 2);
        int left = -size;
        int top = -size;
        int right = size;
        int bottom = size;

        canvas.save();

        canvas.translate(mWidth / 2, mHeight / 2);
        //先画底部白色背景圆弧
        canvas.drawArc(new RectF(left, top, right, bottom), 150, 240, false, mPaint);

        if(progress > 100){
            progress = 100;
        }
        if(progress < 0){
            progress = 0;
        }
        if(progress > 0){
            //再画进度圆弧
            mPaint.setColor(Color.parseColor("#5FB1ED"));
            float sweepAngle = progress * 1.0f / total * 240;//240为背景圆弧的总弧度大小
            canvas.drawArc(new RectF(left, top, right, bottom), 150, sweepAngle, false, mPaint);
        }

        canvas.restore();
    }

    private void drawCircle1(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidth1);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius1, mPaint);
    }

    private void drawCircle2(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#5FB1ED"));
        mPaint.setStrokeWidth(mStrokeWidth2);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius2, mPaint);
    }

    public void setProgress(int curProgress){
        progress = curProgress;
        invalidate();
    }
}
