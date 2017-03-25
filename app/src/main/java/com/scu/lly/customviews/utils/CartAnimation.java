package com.scu.lly.customviews.utils;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by lusheep on 2016/7/4.
 */
public class CartAnimation extends Animation{
    private PathMeasure pathMeasure;
    private float[] pos = new float[2];
    public CartAnimation(Path path){
        pathMeasure = new PathMeasure(path,false);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        pathMeasure.getPosTan(pathMeasure.getLength() * interpolatedTime, pos, null);
        t.getMatrix().setTranslate(pos[0],pos[1]);
    }
}
