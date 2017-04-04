package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * 为Bitmap添加自定义颜色的阴影效果
 * (原图除了图像内容其它最好全为透明像素，否则只有整个图片的边缘会有阴影效果：因为原图是直接绘制在阴影图上的，只会有一个dx和dy的偏移)
 * Created by lusheep on 2017/4/4.
 */

public class ShadowBitmapView extends View {

    private Paint mPaint;
    private Bitmap mSrcBitmap;
    private Bitmap mAlphaBmp;
    private int mDx, mDy;
    private int mShadowColor;
    private float mRadius;

    public ShadowBitmapView(Context context, AttributeSet attrs) throws Exception {
        this(context, attrs, 0);
    }

    public ShadowBitmapView(Context context, AttributeSet attrs, int defStyleAttr) throws Exception {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) throws Exception {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowBitmapView);
        int srcId = ta.getResourceId(R.styleable.ShadowBitmapView_src, -1);
        if(srcId == -1){
            throw new Exception("ShadowBitmapView 需要定义Src属性,而且必须是图像");
        }
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), srcId);

        mDx = ta.getInt(R.styleable.ShadowBitmapView_shadowDx, 0);
        mDy = ta.getInt(R.styleable.ShadowBitmapView_shadowDy, 0);
        mShadowColor = ta.getColor(R.styleable.ShadowBitmapView_shadowColor, Color.BLACK);
        mRadius = ta.getFloat(R.styleable.ShadowBitmapView_shadowRadius, 0.0f);

        ta.recycle();

        mPaint = new Paint();
        mAlphaBmp = mSrcBitmap.extractAlpha();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width = mSrcBitmap.getWidth();
        int height = mSrcBitmap.getHeight();

        setMeasuredDimension(measureWidthMode == MeasureSpec.EXACTLY ? measureWidth : width,
                measureHeightMode == MeasureSpec.EXACTLY ? measureHeight : height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() - mDx;
        int height = width * mSrcBitmap.getHeight() / mSrcBitmap.getWidth();

        //绘制阴影
        mPaint.setColor(mShadowColor);
        mPaint.setMaskFilter(new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.NORMAL));
        canvas.drawBitmap(mAlphaBmp, null, new Rect(mDx, mDy, width + mDx, height + mDy), mPaint);

        //绘制原图像
        mPaint.setMaskFilter(null);
        canvas.drawBitmap(mSrcBitmap, null, new Rect(0, 0, width, height), mPaint);
    }
}
