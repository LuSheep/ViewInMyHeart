package com.scu.lly.customviews.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.view.imageloader.ImageLoader;

import java.util.List;

/**
 * Created by lusheep on 2016/8/10.
 */
public class ImageLoaderAdapter extends BaseAdapter {
    private List<String> urls;
    private LayoutInflater mLayoutInflater;
    private Drawable mDefaultDrawable;

    private ImageLoader mImageLoader;
    private int mImageWidth;
    private boolean mIsGridViewIdle = true;

    public ImageLoaderAdapter(Context context, List<String> urls, int imageWidth){
        this.urls = urls;
        mImageWidth = imageWidth;
        mLayoutInflater = LayoutInflater.from(context);
        mDefaultDrawable = context.getResources().getDrawable(R.drawable.image_default);
        mImageLoader = ImageLoader.build(context);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item_imageloader_gridview, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_grid_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ImageView imageView = holder.imageView;
        String tag = (String) imageView.getTag();
        String url = (String) getItem(position);
        if(!url.equals(tag)){
            imageView.setImageDrawable(mDefaultDrawable);
        }

        if(mIsGridViewIdle){//只有在列表闲置没有滑动的状态下才去加载图片
            imageView.setTag(url);
            mImageLoader.bindBitmap(url, imageView, mImageWidth, mImageWidth);
        }
        return convertView;
    }

    private static class ViewHolder{
        public ImageView imageView;
    }

    public void setGridViewIdle(boolean isIdle){
        mIsGridViewIdle = isIdle;
        if(isIdle){
            notifyDataSetChanged();
        }
    }
}
