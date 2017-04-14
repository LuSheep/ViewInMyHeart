package com.scu.lly.customviews.view.newstudyer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * Created by lusheep on 2017/4/14.
 */

public class WeixinEyesView extends View {

    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;

    private int mRadius = 50;//中间圆的半径
    private int mArcRadius = 20;//圆弧的半径

    private int mArcStrokeWidth = 16;//两段小弧的strokeWidth
    private int mStrokeWidth = 3;//普通线的strokeWidth

    private int length;//圆中心点到眼睛最远的距离
    private int controllLength;//圆中心到控制点的距离

    private float arcAlpha;
    private float circleAlpha;
    private float pathAlpha;

    private int progress;
    private int total = 100;

    //Path最终完全显示时，两边坐标的x、y坐标
    private int mStartX;
    private int mEndX;

    //Path刚开始显示时，两边坐标的x、y坐标
    private int mAppearStartX;
    private int mAppearStartY1;
    private int mAppearStartY2;
    private int mAppearEndX;

    private int mAppearControllY1;
    private int mAppearControllY2;

    public WeixinEyesView(Context context) {
        super(context);
        init();
    }

    public WeixinEyesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeixinEyesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);

        mPath = new Path();

        length = 3 * mRadius;
        controllLength = 5 * mRadius / 2;
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

        mStartX = mWidth / 2 - length;
        mEndX = mWidth / 2 + length;

        mAppearStartX = mWidth / 2 - 2 * mRadius;
        mAppearStartY1 = (int) (mHeight / 2 - 0.7 * mRadius);
        mAppearStartY2 = (int) (mHeight / 2 + 0.7 * mRadius);
        mAppearEndX = mWidth / 2 + 2 * mRadius;

        mAppearControllY1 = (int) (mHeight / 2 - 1.5 * mRadius);
        mAppearControllY2 = (int) (mHeight / 2 + 1.5 * mRadius);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawArc(canvas);

        drawCircle(canvas);

        drawPath(canvas);
    }

    private void drawPath(Canvas canvas) {
        mPath.reset();
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAlpha((int) (pathAlpha * 255));

        mPath.moveTo(mAppearStartX - pathAlpha * (mAppearStartX - mStartX),
                mAppearStartY1 + (mHeight / 2 - mAppearStartY1) * pathAlpha);
        mPath.quadTo(mWidth / 2, mAppearControllY1 - (mAppearControllY1 - (mHeight / 2 - controllLength)) * pathAlpha,
                mAppearEndX + (mEndX - mAppearEndX) * pathAlpha, mAppearStartY1 + (mHeight / 2 - mAppearStartY1) * pathAlpha);

        canvas.drawPath(mPath , mPaint);

        mPath.reset();

        mPath.moveTo(mAppearStartX - pathAlpha * (mAppearStartX - mStartX),
                mAppearStartY2 - (mAppearStartY2 - mHeight / 2) * pathAlpha);
        mPath.quadTo(mWidth / 2, mAppearControllY2 + ((mHeight / 2 + controllLength) - mAppearControllY2) * pathAlpha,
                mAppearEndX + (mEndX - mAppearEndX) * pathAlpha, mAppearStartY2 - (mAppearStartY2 - mHeight / 2) * pathAlpha);
        canvas.drawPath(mPath , mPaint);

//        mPath.moveTo(mStartX, mHeight / 2);
//        mPath.quadTo(mWidth / 2, mHeight / 2 - controllLength, mEndX, mHeight / 2);
//        mPath.quadTo(mWidth / 2, mHeight / 2 + controllLength, mStartX, mHeight / 2);



//        Path p = new Path();
//        Matrix matrix = new Matrix();
//        matrix.setScale(pathAlpha, 1, mWidth / 2, mHeight / 2);
//        p.addPath(mPath, matrix);
//
//        canvas.drawPath(p, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAlpha((int) (circleAlpha * 255));
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);
    }

    private void drawArc(Canvas canvas) {
        mPaint.setStrokeWidth(mArcStrokeWidth);
        mPaint.setAlpha((int) (arcAlpha * 255));

        int r = mArcRadius + mArcStrokeWidth / 2;

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        canvas.drawArc(new RectF(-r, -r, r, r), 200, 8, false, mPaint);
        canvas.drawArc(new RectF(-r, -r, r, r), 220, 20, false, mPaint);

        canvas.restore();
    }

    /**
     * 进度值分配：20%--弧，30%--圆，50%--Path
     * @param p
     */
    public void setProgress(int p){
        if(p < 0)
            p = 0;
        if(p > total)
            p = total;
        progress = p;
        if(progress <= 20){
            arcAlpha = 1.0f * progress / 20;//20的由来：20% * total，下同
        }else if(progress > 20 && progress <= 50){
            progress = p - 20;
            circleAlpha = 1.0f * progress / 30;
        }else if(progress > 50 && progress <= total){
            progress = p - 50;
            pathAlpha = 1.0f * progress / 50;
        }
        invalidate();
    }
}
