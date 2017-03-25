package com.scu.lly.customviews.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * 图片压缩工具类
 * Created by lusheep on 2016/8/9.
 */
public class ImageUtils {

    /**
     * 从resource中加载图片
     * @param res
     * @param resId
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public static Bitmap getBitmapFromResource(Resources res, int resId,
                                               int desiredWidth, int desiredHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//第一步
        BitmapFactory.decodeResource(res, resId, options);//第二步

        //第三步，计算采样率
        options.inSampleSize = calculateInSampleSize(options, desiredWidth, desiredHeight);
        //第四步
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 从文件描述符中加载文件转成图片
     * @param fd
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public static Bitmap getBitmapFromFileDescriptor(FileDescriptor fd, int desiredWidth, int desiredHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        options.inSampleSize = calculateInSampleSize(options, desiredWidth,desiredHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null ,options);
    }

    /**
     * 根据图片初始宽高和目标宽高，计算采样率
     * @param options
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int desiredWidth, int desiredHeight) {
        if(desiredWidth == 0 || desiredHeight == 0){
            return 1;//不压缩
        }
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;
        int inSampleSize = 1;
        if(rawWidth > desiredWidth || rawHeight > desiredHeight){
            int halfWidth = rawWidth / 2;
            int halfHeight = rawHeight / 2;
            while((halfWidth / inSampleSize) >= desiredWidth
                    && (halfHeight / inSampleSize) >= desiredHeight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
