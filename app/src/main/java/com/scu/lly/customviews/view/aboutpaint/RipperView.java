package com.scu.lly.customviews.view.aboutpaint;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

/**
 * 水波纹按钮效果（RadialGradient的应用）
 * Created by lusheep on 2017/4/5.
 */

public class RipperView extends Button {
    private Paint mPaint;
    private RadialGradient mRadialGradient;
    private int mDefaultRadius = 50;
    private int mCurRadius;
    private ObjectAnimator anim;
    private int mCurX, mCurY;

    public RipperView(Context context) {
        super(context);
        init();
    }

    public RipperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RipperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if(mCurX != x || mCurY != y){
            mCurX = x;
            mCurY = y;
            setRadius(mDefaultRadius);
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            return true;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            if(anim != null && anim.isRunning()){
                anim.cancel();
            }
            if(anim == null){
                anim = ObjectAnimator.ofInt(this, "radius", mDefaultRadius, Math.max(getWidth(), getHeight()));
            }
            anim.setInterpolator(new AccelerateInterpolator());
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setRadius(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.start();
        }
        return super.onTouchEvent(event);
    }

    public void setRadius(int radius) {
        mCurRadius = radius;
        if(mCurRadius > 0){
            mRadialGradient = new RadialGradient(mCurX, mCurY, mCurRadius,
                    0x00ffffff, 0xFF58FAAC, Shader.TileMode.CLAMP);
            mPaint.setShader(mRadialGradient);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mCurX, mCurY, mCurRadius, mPaint);
    }
}
