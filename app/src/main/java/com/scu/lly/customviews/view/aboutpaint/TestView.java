package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/3/26.
 */

public class TestView extends View {

    private Paint mPaint;
    private String text = "harvic\'s blog";

    private Bitmap bitmap;


    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);

        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /*
        int baseLineY = 200;
        int baseLineX = 0 ;

        //画text所占的区域
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        int top = baseLineY + fm.top;
        int bottom = baseLineY + fm.bottom;
        int width = (int)mPaint.measureText(text);
        Rect rect = new Rect(baseLineX,top,baseLineX+width,bottom);

        mPaint.setColor(Color.GREEN);
        canvas.drawRect(rect,mPaint);


        //画text所占的区域()
        int top2 = baseLineY + fm.ascent;
        int bottom2 = baseLineY + fm.descent;
        int width2 = (int)mPaint.measureText(text);
        Rect rect2 = new Rect(baseLineX,top2,baseLineX+width2,bottom2);

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rect2,mPaint);

        //画最小矩形
        Rect minRect = new Rect();
        mPaint.getTextBounds(text,0,text.length(),minRect);
        minRect.top = baseLineY + minRect.top;
        minRect.bottom = baseLineY + minRect.bottom;
        mPaint.setColor(Color.RED);
        canvas.drawRect(minRect,mPaint);

        //写文字
        mPaint.setColor(Color.BLACK);
        canvas.drawText(text, baseLineX, baseLineY, mPaint);
        */

//        Paint paint = getPaint();
//        Path p = new Path();
//        p.moveTo(100, 100);
//        p.lineTo(500, 500);
//        paint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
//
//        canvas.drawPath(p, paint);
//
//        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
//                1,0,0,0,0,
//                0,1,0,0,0,
//                0,0,1,0,0,
//                0,0,0,1,0
//        });
//
//        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));


//        mPaint.setARGB(255, 200, 100, 100);
//
//        canvas.drawRect(0, 0, 500, 600, mPaint);
//
//        canvas.translate(550, 0);
//        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
//                0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0,
//                0, 0, 1, 0, 0,
//                0, 0, 0, 1, 0,
//        });
//        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
//        canvas.drawRect(0, 0, 500, 600, mPaint);

//        canvas.drawBitmap(bitmap, null, new Rect(0, 0, 500, 500 * bitmap.getHeight() / bitmap.getWidth()), mPaint);
//
//        canvas.translate(0, 510);
//        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
//                0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0,
//                0, 0, 1, 0, 0,
//                0, 0, 0, 1, 0,
//        });
//        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
//        canvas.drawBitmap(bitmap, null, new Rect(0, 0, 500, 500 * bitmap.getHeight() / bitmap.getWidth()), mPaint);

//        int width = 500;
//        int height = width * bitmap.getHeight() / bitmap.getWidth();
//
//        canvas.drawBitmap(bitmap, null, new Rect(0, 0, width, height), mPaint);
//
//        canvas.translate(0, 520);
//
//        mPaint.setColorFilter(new LightingColorFilter(0xffffff,0x0000f0));
//        canvas.drawBitmap(bitmap, null, new Rect(0, 0, width, height), mPaint);

        int width = 500;
        int height = width * bitmap.getHeight() / bitmap.getWidth();

        mPaint.setColor(Color.RED);

        int layerID = canvas.saveLayer(0, 0, width, height, mPaint, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bitmap, null, new Rect(0, 0, width, height), mPaint);
//        mPaint.setXfermode(new AvoidXfermode(Color.WHITE,100, AvoidXfermode.Mode.TARGET));


        canvas.restoreToCount(layerID);

    }

    private Paint getPaint(){
        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        return paint;
    }
}
