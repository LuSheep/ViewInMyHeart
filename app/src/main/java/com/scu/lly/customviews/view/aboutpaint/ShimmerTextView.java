package com.scu.lly.customviews.view.aboutpaint;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 闪动文字效果
 * Created by lusheep on 2017/4/4.
 */

public class ShimmerTextView extends TextView {

    private Paint mPaint;
    private int dx;
    private LinearGradient mLinearGradient;

    public ShimmerTextView(Context context) {
        super(context);
        init();
    }

    public ShimmerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShimmerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = getPaint();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ValueAnimator anim = ValueAnimator.ofInt(0, 2 * getMeasuredWidth());
        anim.setDuration(3000);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.start();

        mLinearGradient = new LinearGradient(-getMeasuredWidth(), 0, 0, 0, new int[]{
                getCurrentTextColor(), 0xff00ff00, getCurrentTextColor()
        }, new float[]{0, 0.5f, 0}, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(dx, 0);
        mLinearGradient.setLocalMatrix(matrix);
        mPaint.setShader(mLinearGradient);

        super.onDraw(canvas);
    }
}
