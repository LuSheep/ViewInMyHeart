package com.scu.lly.customviews.view.aboutpaint;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.scu.lly.customviews.R;

/**
 * 在背景字内实现波纹效果
 * Created by lusheep on 2017/3/31.
 */

public class CircleWaveView_DSTIN extends View {

    private Paint mPaint;

    private Path mPath;

    private Bitmap bitmapSrc;//文字背景作为SRC
    private Bitmap bitmapDst;//波纹作为DST

    private int mItemWaveLength = 1000;
    private int dx;

    public CircleWaveView_DSTIN(Context context) {
        super(context);
        init();
    }

    public CircleWaveView_DSTIN(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleWaveView_DSTIN(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPath = new Path();

        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.text_shade);
        bitmapDst = Bitmap.createBitmap(bitmapSrc.getWidth(), bitmapSrc.getHeight(), Bitmap.Config.ARGB_8888);

        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        generateWavePath();


        //先清空bitmap上的图像,然后再画上Path
        Canvas c = new Canvas(bitmapDst);
        c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        //此时，将path画到了bitmapDst上，bitmapDst已准备好(由于这里c是new出来的，并不是View当中的canvas，因此此时并不会显示到屏幕上)
        c.drawPath(mPath, mPaint);

        //这里先把文字背景画上，是为了避免后面相交时显示不完全
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bitmapDst, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }

    /**
     * 生成此时的Path
     */
    private void generateWavePath() {
        mPath.reset();
        int waveY = bitmapSrc.getHeight() / 2;
        int haleWaveLength = mItemWaveLength / 2;
        mPath.moveTo(-mItemWaveLength + dx, waveY);
        for(int i = -mItemWaveLength; i < mItemWaveLength + getWidth(); i += mItemWaveLength){
            mPath.rQuadTo(haleWaveLength / 2, -50, haleWaveLength, 0);
            mPath.rQuadTo(haleWaveLength / 2, 50, haleWaveLength, 0);
        }
        mPath.lineTo(bitmapSrc.getWidth(), bitmapSrc.getHeight());
        mPath.lineTo(0, bitmapSrc.getHeight());
        mPath.close();
    }

    private void startAnim(){
        ValueAnimator anim = ValueAnimator.ofInt(0, mItemWaveLength);
        anim.setDuration(2000);
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
