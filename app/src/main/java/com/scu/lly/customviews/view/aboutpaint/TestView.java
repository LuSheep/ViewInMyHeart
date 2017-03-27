package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by lusheep on 2017/3/26.
 */

public class TestView extends View {

    private Paint mPaint;
    private String text = "harvic\'s blog";


    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(120);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d("TAG", "ascent:" + mPaint.ascent() + ", descent:" + mPaint.descent());

        int baseLineY = 200;
        int baseLineX = 0 ;

        //画text所占的区域
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        int top = baseLineY + fm.top;
        int bottom = baseLineY + fm.bottom;
        int width = (int)mPaint.measureText(text);
        Rect rect = new Rect(baseLineX,top,baseLineX+width,bottom);

        mPaint.setColor(Color.GREEN);
        canvas.drawRect(rect,mPaint);


        //画text所占的区域()
        int top2 = baseLineY + fm.ascent;
        int bottom2 = baseLineY + fm.descent;
        int width2 = (int)mPaint.measureText(text);
        Rect rect2 = new Rect(baseLineX,top2,baseLineX+width2,bottom2);

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rect2,mPaint);

        //画最小矩形
        Rect minRect = new Rect();
        mPaint.getTextBounds(text,0,text.length(),minRect);
        minRect.top = baseLineY + minRect.top;
        minRect.bottom = baseLineY + minRect.bottom;
        mPaint.setColor(Color.RED);
        canvas.drawRect(minRect,mPaint);

        //写文字
        mPaint.setColor(Color.BLACK);
        canvas.drawText(text, baseLineX, baseLineY, mPaint);

    }
}
