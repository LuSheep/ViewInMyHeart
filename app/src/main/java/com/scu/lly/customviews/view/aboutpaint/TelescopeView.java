package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * 望远镜效果（考察的是TileModel与绘制位置的关系）
 * Created by lusheep on 2017/4/4.
 */

public class TelescopeView extends View {

    private Paint mPaint;

    private Bitmap mBitmap;
    private Bitmap mBitmapBg;//拉伸mBitmap，使其铺满整个控件

    private int mRadius = 200;//绘制的望远镜半径

    private int mRadiusX = 200, mRadiusY = 200;//绘制的望远镜的圆的圆心

    public TelescopeView(Context context) {
        super(context);
        init();
    }

    public TelescopeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TelescopeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.scenery);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mBitmapBg == null){
            mBitmapBg = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(mBitmapBg);
            c.drawBitmap(mBitmap, null, new Rect(0, 0, getWidth(), getHeight()), mPaint);
        }

        mPaint.setShader(new BitmapShader(mBitmapBg, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        canvas.drawCircle(mRadiusX, mRadiusY, mRadius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mRadiusX = (int) event.getX();
        mRadiusY = (int) event.getY();
        invalidate();
        return true;
    }
}
