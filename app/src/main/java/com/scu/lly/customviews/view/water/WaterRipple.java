package com.scu.lly.customviews.view.water;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * 水波纹效果布局控件，需要实现点击产生水波纹的控件，只需要放在这个布局控件下即可；
 * 绘制思路：
 * 1、先根据触摸事件得到布局下的具体某个被点击的目标控件；
 * 2、获取到目标控件和点击位置后，即可计算出本次绘制的最大半径；
 * 3、计算完半径后，即可进行绘制圆形的操作了。
 * （圆形的绘制放在了dispatchDraw中，并且让下面的子View先绘制完后再进行圆形的绘制，以避免绘制出的圆形被子View给遮盖）
 * --------------
 * 缺陷：
 * 对于矩形类控件有效，对于那些使用了shape作为圆角背景的控件来说，绘制水波纹的时候。绘制出的还是一个矩形，而不是相应的圆角矩形
 * Created by lusheep on 2016/7/1.
 */
public class WaterRipple extends LinearLayout{

    /**
     * 当前点击的控件
     */
    private View mTargetView;
    /**
     * 水波纹绘制刷新时间
     */
    private int INVALIDATE_DURATION = 40;
    /**
     * 手指点击的事件位于水波纹控件中的位置，也就是绘图圆点
     */
    private int mCenterX;
    private int mCenterY;
    /**
     * 当前绘制的半径
     */
    private int mCurRadius;
    /**
     * 最大能达到的半径
     */
    private int mMaxRadius;
    /**
     * 点击目标控件的宽度和高度
     */
    private int mTargetViewWidth,mTargetViewHeight;
    /**
     * 水波纹控件在屏幕所处位置
     */
    private int[] mLocation = new int[2];

    /**
     * 目标控件宽度和高度中的较小值
     */
    private int mTargetViewMinSize;
    /**
     * 半径之间的距离（即每次半径增加的幅度）
     */
    private int mRadiusGap;
    /**
     * 是否处于按下状态
     */
    private boolean mIsPressed = false;
    /**
     * 是否应该进行水波纹绘制动画（可用来判断水波纹绘制是否结束）
     */
    private boolean mShouldDoAnimal = false;
    /**
     * 手指按下和手指抬起时，是否位于同一个View上
     */
    private boolean mIsInOneView = false;

    private Paint mPaint;

    private OnCompleteAnimation onCompleteListener;
    public void setOnCompleteListener(OnCompleteAnimation l){
        onCompleteListener = l;
    }

    /**
     * 处理动画完成后的事件回调，可以用来代替点击事件；
     * 设置了这个监听就不要再设置控件的点击事件了，否则会有两次点击事件
     */
    public interface OnCompleteAnimation{
        void onComplete(int id);
    }

    public WaterRipple(Context context) {
        super(context);
        init();
    }

    public WaterRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterRipple(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#3b4169E1"));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getLocationOnScreen(mLocation);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN://在点击下去的时候，需要判断出点击的是哪个控件
                mTargetView = getTargetView(this, x, y);
                if(mTargetView != null && mTargetView.isEnabled()){
                    initParamsForChild(ev,mTargetView);
                    postInvalidateDelayed(INVALIDATE_DURATION);
                }
                break;
            case MotionEvent.ACTION_UP:
                //确定手指抬起时的位置是否和手指按下的位置位于同一个View上
                judgeIsInOneView(x, y);
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取当前点击的View控件
     * @param view
     * @param x
     * @param y
     * @return
     */
    private View getTargetView(View view, int x, int y) {
        View targetView = null;
        ArrayList<View> allTouchableViews = view.getTouchables();
        for(View v : allTouchableViews){
            if(isTargetView(v, x, y)){
                targetView = v;
                break;
            }
        }
        return targetView;
    }

    /**
     * 判断点击事件的x、y坐标是否在该控件上;
     * 通过（x，y）是否落在View的矩阵当中
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isTargetView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //下面的坐标是以相对屏幕的绝对坐标来计算的
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if(x >= left && x <= right && y >= top && y <= bottom){
            return true;
        }
        return false;
    }

    /**
     * 为该点击的控件初始化一些参数：
     * @param ev
     * @param view
     */
    private void initParamsForChild(MotionEvent ev, View view) {
        /**
         * 注意：这里的MotionEvent事件是位于水波纹控件中的，事件还没有往下分发，
         * 因此这里通过ev.getX,ev.getY获取的是事件相对于水波纹控件中的左边点，而不是具体被点击到的控件中的坐标值
         */
        mCenterX = (int) ev.getX();
        mCenterY = (int) ev.getY();

        mTargetViewWidth = view.getMeasuredWidth();
        mTargetViewHeight = view.getMeasuredHeight();
        mTargetViewMinSize = Math.min(mTargetViewWidth, mTargetViewHeight);

        mCurRadius = 0;//初始半径置为0
        mRadiusGap = mTargetViewMinSize / 8;
        mIsPressed = true;
        mShouldDoAnimal = true;

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int mTargetViewLeft = location[0] - mLocation[0];
        int mTargetViewTop = location[1] - mLocation[1];

        int leftInTargetView = mCenterX - mTargetViewLeft;
        int topInTargetView = mCenterY - mTargetViewTop;

        int maxWidth = Math.max(leftInTargetView, mTargetViewWidth - leftInTargetView);
        int maxHeight = Math.max(topInTargetView, mTargetViewHeight - topInTargetView);

        mMaxRadius = Math.max(maxWidth, maxHeight);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mTargetView == null || !mShouldDoAnimal || mTargetViewWidth < 0){
            return;
        }
        //更新当前的绘制半径
        if(mCurRadius > mTargetViewMinSize / 4){
            mCurRadius += mRadiusGap * 2;
        }else{
            mCurRadius += mRadiusGap;
        }
        //下面计算目标控件位于水波纹控件中的位置，准备绘图使用
        int[] location = new int[2];
        mTargetView.getLocationOnScreen(location);
        int left = location[0] - mLocation[0];
        int top = location[1] - mLocation[1];
        int right = left + mTargetView.getMeasuredWidth();
        int bottom = top + mTargetView.getMeasuredHeight();

        canvas.save();
        canvas.clipRect(left,top,right,bottom);
        canvas.drawCircle(mCenterX,mCenterY,mCurRadius,mPaint);
        canvas.restore();

        //循环刷新绘制，直到半径达到最大值
        if(mCurRadius <= mMaxRadius){
            postInvalidateDelayed(INVALIDATE_DURATION,left,top,right,bottom);
        }else if(!mIsPressed){
            mShouldDoAnimal = false;
            postInvalidateDelayed(INVALIDATE_DURATION,left,top,right,bottom);
            //外面实现这个接口，可以在动画完成后进行相应的动作，如点击事件
            if(onCompleteListener != null && mIsInOneView){
                onCompleteListener.onComplete(mTargetView.getId());
            }
        }
    }

    /**
     * 判断是否位于同一个View上
     * @param x
     * @param y
     */
    private void judgeIsInOneView(int x, int y) {
        if(mTargetView != null){
            mIsInOneView = isTargetView(mTargetView,x,y);
        }else{
            mIsInOneView = false;
        }
    }

}
