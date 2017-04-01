package com.scu.lly.customviews.view.aboutpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.R;

/**
 * 通过使用xfermode的形式设置图片圆角（这里只是一个简单案例，项目中可以使用本项目中的CustomImageView实现）
 * Created by lusheep on 2017/3/31.
 */

public class XfermodeCircleImageView extends View {

    private Bitmap bitmapDst;
    private Bitmap bitmapSrc;
    private Paint mPaint;

    public XfermodeCircleImageView(Context context) {
        super(context);
        init();
    }

    public XfermodeCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XfermodeCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        bitmapDst = BitmapFactory.decodeResource(getResources(), R.mipmap.dog_shade);
        bitmapSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bitmapDst, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }
}
