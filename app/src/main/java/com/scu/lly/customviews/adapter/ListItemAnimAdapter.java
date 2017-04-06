package com.scu.lly.customviews.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.scu.lly.customviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView进场动画的Adapter
 * Created by lusheep on 2017/4/6.
 */

public class ListItemAnimAdapter extends BaseAdapter {

    private List<Drawable> mDatas = new ArrayList<Drawable>();
    private LayoutInflater mInflater;
    private Context mContext;
    private int mLength;
    private ListView listView;

    private Animation anim;

    private int mLastFirstVisible;

    private boolean isScrollUp;//表示手指是否往上滑
    private int mLastTop;

    public ListItemAnimAdapter(List<Drawable> data, Context context, int length, ListView l){
        mDatas.addAll(data);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLength = length;
        listView = l;

        anim = AnimationUtils.loadAnimation(context, R.anim.anim_listitem);

        listView.setOnScrollListener(mOnScrollListener);
    }

    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            View firstChild = view.getChildAt(0);
            if(firstChild == null)
                return;
            int top = firstChild.getTop();
            //判断是否上滑
            isScrollUp = firstVisibleItem > mLastFirstVisible || mLastTop > top;
            mLastTop = top;
            mLastFirstVisible = firstVisibleItem;
        }
    };

    @Override
    public int getCount() {
        return mLength;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position % mDatas.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_listcustomanim, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.item_listcustomanim_img);
            holder.tv = (TextView) convertView.findViewById(R.id.item_listcustomanim_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //清除当前显示区域中所有item的动画
        for(int i = 0; i < listView.getChildCount(); i++){
            listView.getChildAt(i).clearAnimation();
        }

        if(isScrollUp){//只有上滑的时候才进行动画
            convertView.startAnimation(anim);
        }

        Drawable drawable = (Drawable) getItem(position);
        holder.iv.setImageDrawable(drawable);
        holder.tv.setText("" + position);
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
