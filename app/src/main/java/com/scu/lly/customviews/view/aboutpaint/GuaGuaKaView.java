package com.scu.lly.customviews.view.aboutpaint;

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
import android.view.MotionEvent;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * 刮刮卡View
 * Created by lusheep on 2017/3/31.
 */

public class GuaGuaKaView extends View {

    private Paint mPaint;
    private Bitmap bitmapSrc;//刮奖封面作为SRC
    private Bitmap bitmapDst;//手势形成的bitmap作为DST
    private Bitmap bitmapResult;//底部的刮奖结果

    private Path mPath;

    private int mLastX;
    private int mLastY;

    public GuaGuaKaView(Context context) {
        super(context);
        init();
    }

    public GuaGuaKaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuaGuaKaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(30);

        mPath = new Path();

        bitmapResult = BitmapFactory.decodeResource(getResources(), R.mipmap.guaguaka_text);
        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.guaguaka_pic);
        bitmapDst = Bitmap.createBitmap(bitmapSrc.getWidth(), bitmapSrc.getHeight(), Bitmap.Config.ARGB_8888);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                mLastX = x;
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                int endX = (mLastX + x) / 2;
                int endY = (mLastY + y) / 2;
                mPath.quadTo(mLastX, mLastY, endX, endY);
                mLastX = x;
                mLastY = y;
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先把获奖结果画上
        canvas.drawBitmap(bitmapResult, 0, 0, mPaint);

        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        Canvas c = new Canvas(bitmapDst);
        c.drawPath(mPath, mPaint);

        //把手势作为DET画上
        canvas.drawBitmap(bitmapDst, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }
}
