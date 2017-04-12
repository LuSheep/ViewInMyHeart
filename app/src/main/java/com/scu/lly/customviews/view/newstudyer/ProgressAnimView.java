package com.scu.lly.customviews.view.newstudyer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.utils.CommonUtils;

/**
 * 动画特效的progress
 * Created by lusheep on 2017/4/9.
 */

public class ProgressAnimView extends View {

    private Paint mPaint;
    private Path arrowPath;
    private Path progressPath;
    private int mRectSize;//中间矩形大小、宽高一致
    //控件宽高
    private int mWidth;
    private int mHeight;

    private int rectPercent;
    private int rightPercent;
    private int leftPercent;
    private int progressPercent;

    private float left, top, right, bottom;
    private float progressSize = 10;//变成线时高度（一半高度）
    private float dashWidth;//变成线时离左右两边的距离

    private Matrix matrix;

    private float rotateDegress;//右边的旋转角度

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(500);
            while(rectPercent < 100){
                rectPercent += 5;
                postInvalidate();
                SystemClock.sleep(20);
            }
            while(rightPercent < 100){//先右移
                rightPercent += 5;
                postInvalidate();
                SystemClock.sleep(20);
            }
            while(leftPercent < 100){//再左移动
                leftPercent += 5;
                postInvalidate();
                SystemClock.sleep(20);
            }
            while(progressPercent < 50){
                progressPercent += 2;
                postInvalidate();
                SystemClock.sleep(20);
            }
        }
    };

    public ProgressAnimView(Context context) {
        super(context);
        init();
    }

    public ProgressAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        arrowPath = new Path();
        progressPath = new Path();

        matrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        if(heightMode ==  MeasureSpec.EXACTLY){
            mHeight = (int) CommonUtils.dp2px(getContext(), 200);
        }

        mRectSize = mHeight / 2;

        left = 1.0f * mWidth / 2 - 1.0f * mRectSize / 2;
        top = 1.0f * mRectSize / 2;
        right = 1.0f * mWidth / 2 + 1.0f * mRectSize / 2;
        bottom = 3 * 1.0f * mRectSize / 2;

        dashWidth = mRectSize / 4;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawArrow(canvas);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setPathEffect(new CornerPathEffect(progressSize));//设置进度条的圆角，与灰色的progressWidth一致
        float moveX = 1.0f * (mWidth - mRectSize / 2) * progressPercent / 100;//总长度-两边左右边距
        mPaint.setColor(Color.WHITE);
        progressPath.reset();
        progressPath.moveTo(dashWidth, mRectSize - progressSize);
        progressPath.lineTo(dashWidth + moveX, mRectSize - progressSize);
        progressPath.lineTo(dashWidth + moveX, mRectSize + progressSize);
        progressPath.lineTo(dashWidth, mRectSize + progressSize);
        progressPath.close();
        canvas.drawPath(progressPath, mPaint);
    }

    private void drawArrow(Canvas canvas) {
        if(rectPercent > 0){
            mPaint.setPathEffect(new CornerPathEffect(5));//设置变形后箭头的圆角
        }
        int upPercent = rectPercent;//上升的时间
        int downPercent = 0;//下降的时间
        if(rectPercent > 60){
            upPercent = 60;
        }
        if(rectPercent > 60 && rectPercent <= 100){
            downPercent = rectPercent - 60;
        }

        if(rightPercent > 0){
            rotateDegress = 15.0f * rightPercent / 100;
        }
        if(leftPercent == 100){
            rotateDegress = 0;//移动到了最左边，不需要旋转了
        }

        float shapeSize = 1.0f * mRectSize / 8 * rectPercent / 100;//形状改变量
        float arrowUp = 1.0f * mRectSize / 2 * upPercent / 60;
        float arrowDown = (1.0f * mRectSize / 4 + 1.2f * mRectSize / 8 - progressSize) * downPercent / 40;
        float arrowRight = 1.0f * mRectSize / 4 * rightPercent / 100;//向右移动mRectSize / 4，即左右边距距离
        float arrowLeft = 1.0f * (mRectSize / 4 - mWidth / 2 - mRectSize / 4) * leftPercent / 100;
        float moveX = arrowRight + arrowLeft + 1.0f * (mWidth - mRectSize / 2) * progressPercent / 100;
        float moveY = arrowUp - arrowDown;

        arrowPath.reset();
        mPaint.setColor(Color.WHITE);

        arrowPath.moveTo(mWidth / 2 - mRectSize / 8 - shapeSize + moveX,
                3 * mRectSize / 4 - moveY);
        arrowPath.lineTo(mWidth / 2 + mRectSize / 8 + shapeSize + moveX, 3 * mRectSize / 4 - moveY);
        arrowPath.lineTo(mWidth / 2 + mRectSize / 8 + shapeSize + moveX, mRectSize - moveY);
        arrowPath.lineTo(mWidth / 2 + mRectSize / 4 - shapeSize * 1.2f + moveX, mRectSize - moveY);
        arrowPath.lineTo(mWidth / 2 + moveX, mRectSize + mRectSize / 4 - shapeSize * 1.2f - moveY);
        arrowPath.lineTo(mWidth / 2 - mRectSize / 4 + shapeSize * 1.2f + moveX, mRectSize - moveY);
        arrowPath.lineTo(mWidth / 2 - mRectSize / 8 - shapeSize + moveX, mRectSize - moveY);
        arrowPath.close();

        matrix.setRotate(rotateDegress, mWidth / 2 + moveX, mRectSize + mRectSize / 4 - shapeSize * 1.2f - moveY);
        arrowPath.transform(matrix);
        canvas.drawPath(arrowPath, mPaint);
    }

    private void drawBackground(Canvas canvas) {
        RectF rectf = new RectF(getGradientFloat(left, dashWidth, rectPercent, 100),
                getGradientFloat(top, mRectSize - progressSize, rectPercent, 100),
                getGradientFloat(right, mWidth - dashWidth, rectPercent, 100),
                getGradientFloat(bottom, mRectSize + progressSize, rectPercent, 100));
        mPaint.setColor(Color.parseColor("#FF525253"));
        canvas.drawRoundRect(rectf, progressSize, progressSize, mPaint);
    }

    private float getGradientFloat(float from, float to, int progress, int total){
        return from - (from - to) * progress / total;
    }

    public void start(){
        new Thread(timeRunnable).start();
    }
}
