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
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.scu.lly.customviews.R;

/**
 * 心电图效果（实际应该是通过Path把心电图画出来作为SRC，这里只是用了一张心电图图片为例，然后用动画展示）
 * Created by lusheep on 2017/4/1.
 */

public class HeartMapView extends View {

    private Paint mPaint;
    private Bitmap bitmapSrc;//新建的矩形作为SRC
    private Bitmap bitmapDst;//心电图作为DST

    private int mItemLength;
    private int dx;

    public HeartMapView(Context context) {
        super(context);
        init();
    }

    public HeartMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        bitmapDst = BitmapFactory.decodeResource(getResources(), R.mipmap.heartmap);
        bitmapSrc = Bitmap.createBitmap(bitmapDst.getWidth(), bitmapDst.getHeight(), Bitmap.Config.ARGB_8888);

        mItemLength = bitmapDst.getWidth();

        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Canvas c = new Canvas(bitmapSrc);
        c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        //画上矩形，得到矩形图片
        c.drawRect(bitmapDst.getWidth() - dx, 0, bitmapDst.getWidth(), bitmapDst.getHeight(), mPaint);

        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bitmapDst, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }

    private void startAnim(){
        ValueAnimator anim = ValueAnimator.ofInt(0, mItemLength);
        anim.setDuration(6000);
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
