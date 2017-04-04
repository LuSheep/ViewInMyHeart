package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/4/4.
 */

public class AvatorView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;

    public AvatorView(Context context) {
        super(context);
        init();
    }

    public AvatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        int w = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
        float scale = (float) getWidth() / w;

        matrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(matrix);

        mPaint.setShader(mBitmapShader);

        canvas.drawCircle(getWidth() / 2,  getWidth() / 2, getWidth() / 2, mPaint);
    }
}
