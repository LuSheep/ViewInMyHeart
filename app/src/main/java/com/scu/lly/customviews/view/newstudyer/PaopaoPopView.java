package com.scu.lly.customviews.view.newstudyer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * 泡泡弹窗View（底边三角形是一个等边三角形）
 * Created by lusheep on 2017/4/8.
 */

public class PaopaoPopView extends View {

    private Paint mPaint;
    private Path mPath;

    private int mPopTriangleHeight;//三角形高度
    private int mPopColor;//背景颜色

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 50;

    //控件的实际宽高
    private int mWidth;
    private int mHeight;

    //泡泡窗口矩形的宽高
    private int mRectWidth;
    private int mRectHeight;

    private int bottomTriangleHalfWidth;//底边三角形的一半宽度

    private String mText;//需要在pop上绘制的文字
    private int mTextColor;
    private Paint mTextPaint;//绘制文字的画笔

    public PaopaoPopView(Context context) {
        this(context, null);
    }

    public PaopaoPopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaopaoPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PaopaoPopView);
        mPopTriangleHeight = ta.getInt(R.styleable.PaopaoPopView_pop_triangle_height, 30);//默认30的高度
        mPopColor = ta.getColor(R.styleable.PaopaoPopView_pop_color, Color.BLUE);//默认蓝色
        mText = ta.getString(R.styleable.PaopaoPopView_pop_text);
        mTextColor = ta.getColor(R.styleable.PaopaoPopView_pop_text_color, Color.WHITE);
        ta.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPopColor);

        mPath = new Path();

        bottomTriangleHalfWidth = (int) (Math.tan(Math.toRadians(30)) * mPopTriangleHeight);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(25);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = DEFAULT_WIDTH;
        }
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = DEFAULT_HEIGHT;
        }

        mRectWidth = widthSize;
        mRectHeight = heightSize;

        mWidth = widthSize;
        mHeight = heightSize + mPopTriangleHeight;

        setMeasuredDimension(mWidth, mHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawRect(0, 0, mRectWidth, mRectHeight, mPaint);

        canvas.drawRoundRect(0, 0, mRectWidth, mRectHeight, 10, 10, mPaint);

        mPath.reset();

        mPath.moveTo(mWidth / 2 - bottomTriangleHalfWidth, mRectHeight);
        mPath.lineTo(mWidth / 2, mHeight);
        mPath.lineTo(mWidth / 2 + bottomTriangleHalfWidth, mRectHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);

        //居中绘制文字
        int mtextWidth = (int) mTextPaint.measureText(mText);
        canvas.drawText(mText, mRectWidth / 2 - mtextWidth / 2,
                mRectHeight / 2 - (mTextPaint.ascent() + mTextPaint.descent()) / 2, mTextPaint);
    }
}
