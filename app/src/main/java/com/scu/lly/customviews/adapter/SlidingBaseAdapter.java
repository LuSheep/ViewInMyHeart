package com.scu.lly.customviews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.model.SlidingItemModel;
import com.scu.lly.customviews.view.slidingview.SlidingItemLayout;

import java.util.List;

/**
 * Created by lusheep on 2016/6/29.
 */
public class SlidingBaseAdapter extends BaseAdapter{
    private List<SlidingItemModel> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;
    public SlidingBaseAdapter(Context ct, List<SlidingItemModel> data){
        mDatas = data;
        mInflater = LayoutInflater.from(ct);
        mContext = ct;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        SlidingItemLayout itemLayout = null;//真正的滑动布局
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item_sliding, null);
            itemLayout = (SlidingItemLayout) convertView.findViewById(R.id.id_sliding_item_layout);
            holder = new ViewHolder();
            holder.nameTv = (TextView) itemLayout.findViewById(R.id.tv_item_name);
            holder.delTvBtn = (TextView) itemLayout.findViewById(R.id.menu_del);
            holder.updateBtn = (TextView) itemLayout.findViewById(R.id.menu_update);
            holder.slidingItemLayout = itemLayout;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        SlidingItemModel model = (SlidingItemModel) getItem(position);
        model.menuLayout = holder.slidingItemLayout;//注意记得给model中的侧滑布局设值
        model.menuLayout.reset();//滑动中把所有状态重置
        final int p = position;
        holder.delTvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"---delBtn-->" + p , Toast.LENGTH_SHORT).show();
            }
        });
        holder.nameTv.setText(model.name);

        return convertView;
    }

    class ViewHolder{
        TextView nameTv;
        TextView delTvBtn;
        TextView updateBtn;
        SlidingItemLayout slidingItemLayout;
    }
}
