package com.scu.lly.customviews.view.imageloader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 实现一个宽、高相等的正方形ImageView
 * Created by lusheep on 2016/8/10.
 */
public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
