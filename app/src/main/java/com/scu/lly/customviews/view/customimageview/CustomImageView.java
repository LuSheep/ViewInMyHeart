package com.scu.lly.customviews.view.customimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.scu.lly.customviews.R;

/**
 * 自定义圆形头像、圆角头像等
 * Created by lusheep on 2016/7/27.
 */
public class CustomImageView extends ImageView {

    /**
     * 图片的类型（0--圆形，默认，1--圆角）
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;//圆形
    private static final int TYPE_ROUND = 1;//圆角
    /**
     * 圆形半径
     */
    private int mRadius;
    /**
     * 圆角的半径
     */
    private int mRoundRadius;

    private static final int ROUND_RADIUS_DEFAULT = 10;//圆角的半径默认大小

    /**
     * 画笔着色器
     */
    private BitmapShader mBitmapShader;

    /**
     * 着色器变幻矩阵
     */
    private Matrix mMatrix;

    /**
     * View的宽度
     */
    private int mWidth;

    private Paint mBitmapPaint;

    /**
     * 圆角图形所占的矩阵
     */
    private RectF mRoundRectF;
    /**
     * 边框画笔
     */
    private Paint mBorderPaint;
    /**
     * 画边框的半径
     */
    private int mBorderRadius;

    /**
     * 边框大小
     */
    private int mBorderWidth;

    /**
     * 默认边框大小
     */
    private static final int BORDER_WIDTH_DEFAULT = 0;
    /**
     * 边框颜色
     */
    private int mBorderColor;

    private static final int BORDER_Color_DEFAULT = Color.TRANSPARENT;

    /**
     * 画圆角时边框矩阵
     */
    private RectF mBorderRect;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        type = ta.getInt(R.styleable.CustomImageView_type, TYPE_CIRCLE);
        mRoundRadius = ta.getDimensionPixelSize(R.styleable.CustomImageView_roundRadius, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, ROUND_RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        mBorderWidth = ta.getDimensionPixelOffset(R.styleable.CustomImageView_borderWidth, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, BORDER_WIDTH_DEFAULT, getResources().getDisplayMetrics()));
        mBorderColor = ta.getColor(R.styleable.CustomImageView_borderColor,  BORDER_Color_DEFAULT);
        ta.recycle();

        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(type == TYPE_CIRCLE){
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2 - mBorderWidth;
            mBorderRadius = mRadius + mBorderWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(type == TYPE_ROUND){
            mRoundRectF = new RectF(mBorderWidth , mBorderWidth, w - mBorderWidth, h - mBorderWidth);
            mBorderRect = new RectF(0, 0, w ,h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if(getDrawable() == null)
            return;
        //设置着色器
        setShader();
        if(type == TYPE_CIRCLE){
            canvas.drawCircle(mWidth / 2, mWidth / 2, mRadius, mBitmapPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, mBorderRadius, mBorderPaint);
        }else if(type == TYPE_ROUND){
            canvas.drawRoundRect(mRoundRectF, mRoundRadius, mRoundRadius, mBitmapPaint);
            if(mBorderWidth > 0){
                canvas.drawRoundRect(mBorderRect, mRoundRadius, mRoundRadius, mBorderPaint);
            }
        }
    }

    private void setShader() {
        Drawable drawable = getDrawable();
        if(drawable == null)
            return ;
        Bitmap bmp = drawableToBitmap(drawable);
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if(type == TYPE_CIRCLE){
            int bmpsize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bmpsize;
        }else if(type == TYPE_ROUND){
            if(bmp.getWidth() != getWidth() || bmp.getHeight() != getHeight()){//需要进行缩放
                scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
            }
        }
        //shader的变换矩阵，用于放大或者缩小
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        //给画笔设置着色器
        mBitmapPaint.setShader(mBitmapShader);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bm;
    }
}
