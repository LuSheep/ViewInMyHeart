package com.scu.lly.customviews.view.aboutpaint;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.scu.lly.customviews.R;

/**
 * 不规则波纹
 * Created by lusheep on 2017/4/1.
 */

public class IrregularWaveView extends View {

    private Paint mPaint;
    private Bitmap bitmapSrc;//背景圆作为SRC
    private Bitmap bitmapDst;//波纹作为DST

    private int mItemLength;
    private int dx;

    public IrregularWaveView(Context context) {
        super(context);
        init();
    }

    public IrregularWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IrregularWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//这里必须要开启硬件加速，否则播放动画不连续
        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setColor(Color.RED);

        bitmapDst = BitmapFactory.decodeResource(getResources(), R.mipmap.wave_bg, null);
        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_shape, null);

        mItemLength = bitmapDst.getWidth();
        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先画上圆形
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bitmapDst, new Rect(dx, 0, dx + bitmapSrc.getWidth(), bitmapSrc.getHeight()),
                new Rect(0 ,0 ,bitmapSrc.getWidth(), bitmapSrc.getHeight()), mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmapSrc, 0 ,0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }

    private void startAnim(){
        ValueAnimator anim = ValueAnimator.ofInt(0, mItemLength);
        anim.setDuration(4000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.start();
    }
}
