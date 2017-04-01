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
 * 橡皮擦View
 * Created by lusheep on 2017/3/31.
 */

public class EraserView extends View {

    private Bitmap bitmapDst;
    private Bitmap bitmapSrc;
    private Paint mPaint;
    private Path mPath;

    private int mLastX;
    private int mLastY;

    public EraserView(Context context) {
        super(context);
        init();
    }

    public EraserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EraserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(30);

        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.dog, null);
        bitmapDst = Bitmap.createBitmap(bitmapSrc.getWidth(), bitmapSrc.getHeight(), Bitmap.Config.ARGB_8888);
        mPath = new Path();
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

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        //先把手指轨迹画到目标Bitmap上
        Canvas c = new Canvas(bitmapDst);
        c.drawPath(mPath, mPaint);

        //然后把目标图像画到画布上
        canvas.drawBitmap(bitmapDst, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }
}
