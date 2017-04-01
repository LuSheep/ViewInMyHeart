package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * Created by lusheep on 2017/3/31.
 */

public class XfermodeInvertedView extends View {

    private Bitmap bitmapDst;
    private Bitmap bitmapSrc;
    private Bitmap bitmapInvert;
    private Paint mPaint;

    public XfermodeInvertedView(Context context) {
        super(context);
        init();
    }

    public XfermodeInvertedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XfermodeInvertedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        bitmapDst = BitmapFactory.decodeResource(getResources(), R.mipmap.dog_invert_shade);
        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);

        Matrix matrix = new Matrix();
        matrix.setScale(1F, -1F);
        //生成倒影图
        bitmapInvert = Bitmap.createBitmap(bitmapSrc, 0, 0, bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先画出小狗图片
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        canvas.translate(0, bitmapSrc.getHeight());

        canvas.drawBitmap(bitmapDst, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapInvert, 0, 0, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerID);
    }
}
