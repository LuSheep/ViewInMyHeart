package com.scu.lly.customviews.view.redpointview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.scu.lly.customviews.R;

/**
 * 仿QQ红点拖拽效果
 * Created by lusheep on 2017/4/1.
 */

public class RedPointView extends FrameLayout {

    private Paint mPaint;
    private Path mPath;

    private static final float DEFAULT_RADIUS = 20;

    private float mRadius = DEFAULT_RADIUS;

    private PointF mStartPoint;
    private PointF mCurPoint;

    private boolean mIsTouchStatus;

    private TextView mNumberTextView;//实现数量的TextView
    private ImageView mExploredImageView;//用于展示爆炸效果的ImageView
    private boolean isAnimStart;//是否可以开始演示爆炸效果

    public RedPointView(Context context) {
        super(context);
        init();
    }

    public RedPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RedPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mStartPoint = new PointF(100, 100);
        mCurPoint = new PointF();

        mPath = new Path();

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mNumberTextView = new TextView(getContext());
        mNumberTextView.setLayoutParams(lp);
        mNumberTextView.setPadding(10, 10, 10, 10);
        mNumberTextView.setBackgroundResource(R.drawable.text_red_point);
        mNumberTextView.setTextColor(Color.WHITE);
        mNumberTextView.setText("99+");

        mExploredImageView = new ImageView(getContext());
        mExploredImageView.setLayoutParams(lp);
        mExploredImageView.setImageResource(R.drawable.tip_anim);
        mExploredImageView.setVisibility(INVISIBLE);

        addView(mNumberTextView);
        addView(mExploredImageView);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        if(!mIsTouchStatus || isAnimStart){
            mNumberTextView.setX(mStartPoint.x - mNumberTextView.getWidth() / 2);
            mNumberTextView.setY(mStartPoint.y - mNumberTextView.getHeight() / 2);
            //下面这种移动方式也可以
//            int l = (int) (mStartPoint.x - mNumberTextView.getWidth() / 2);
//            int t = (int) (mStartPoint.y - mNumberTextView.getHeight() / 2);
//            mNumberTextView.layout(l, t, l + mNumberTextView.getWidth(), t + mNumberTextView.getHeight());
        }else{
            calculatePath();
            canvas.drawCircle(mStartPoint.x, mStartPoint.y, mRadius, mPaint);
            canvas.drawCircle(mCurPoint.x, mCurPoint.y, mRadius, mPaint);
            canvas.drawPath(mPath, mPaint);
            //将textview的中心放在当前手指位置
            mNumberTextView.setX(mCurPoint.x - mNumberTextView.getWidth() / 2);
            mNumberTextView.setY(mCurPoint.y - mNumberTextView.getHeight() / 2);
            //下面这种移动方式也可以
//            int l = (int) (mCurPoint.x - mNumberTextView.getWidth() / 2);
//            int t = (int) (mCurPoint.y - mNumberTextView.getHeight() / 2);
//            mNumberTextView.layout(l, t, l + mNumberTextView.getWidth(), t + mNumberTextView.getHeight());
        }

        canvas.restore();

        super.dispatchDraw(canvas);
    }

    private void calculatePath() {
        mPath.reset();
        float x = mCurPoint.x;
        float y = mCurPoint.y;
        float startX = mStartPoint.x;
        float startY = mStartPoint.y;
        float dx = x - startX;
        float dy = y - startY;

        double a = Math.atan(dy / dx);
        float offsetX = (float) (mRadius * Math.sin(a));
        float offsetY = (float) (mRadius * Math.cos(a));

        //根据相应规则计算当前半径
        float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        mRadius = DEFAULT_RADIUS - distance / 15;
        if(mRadius < 9){//演示爆炸效果
//            mRadius = 9;
            isAnimStart = true;
            mExploredImageView.setX(mCurPoint.x - mNumberTextView.getWidth() / 2);
            mExploredImageView.setY(mCurPoint.y - mNumberTextView.getHeight() / 2);
            mExploredImageView.setVisibility(VISIBLE);
            ((AnimationDrawable)(mExploredImageView.getDrawable())).start();

            mNumberTextView.setVisibility(GONE);
        }

        float x1 = startX + offsetX;
        float y1 = startY - offsetY;

        float x2 = x + offsetX;
        float y2 = y - offsetY;

        float x3 = x - offsetX;
        float y3 = y + offsetY;

        float x4 = startX - offsetX;
        float y4 = startY + offsetY;

        float controlX = (startX + x) / 2;
        float controlY = (startY + y) / 2;

        mPath.moveTo(x1, y1);
        mPath.quadTo(controlX, controlY, x2, y2);
        mPath.lineTo(x3, y3);
        mPath.quadTo(controlX, controlY, x4, y4);
        mPath.lineTo(x1, y1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isInTouchView(event, mNumberTextView)){
                    mIsTouchStatus = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsTouchStatus = false;
                break;
        }
        mCurPoint.set(x, y);
        postInvalidate();
        return true;
    }

    /**
     * 判断当前点击位置是否在目标View当中
     * @param ev
     * @param targetView
     * @return
     */
    private boolean isInTouchView(MotionEvent ev, View targetView){
        Rect rect = new Rect();
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        rect.left = location[0];
        rect.top = location[1];
        rect.right = location[0] + targetView.getWidth();
        rect.bottom = location[1] + targetView.getHeight();
        int pointX = (int) ev.getRawX();
        int pointY = (int) ev.getRawY();
        if(rect.contains(pointX, pointY)){
            return true;
        }
        return false;
    }
}
