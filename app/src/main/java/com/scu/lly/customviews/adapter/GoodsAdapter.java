package com.scu.lly.customviews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.model.GoodsModel;
import com.scu.lly.customviews.utils.CartAddListener;

import java.util.List;

/**
 * Created by lusheep on 2016/7/4.
 */
public class GoodsAdapter extends BaseAdapter {
    private CartAddListener listener;
    private List<GoodsModel> mDatas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public GoodsAdapter(Context ct, List<GoodsModel> data){
        mDatas = data;
        mContext = ct;
        mLayoutInflater = LayoutInflater.from(ct);
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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_goods_list,null);
            holder.goodsName = (TextView) convertView.findViewById(R.id.item_tv_goodsName);
            holder.goodsPic = (ImageView) convertView.findViewById(R.id.item_iv_goods);
            holder.buyBtn = (TextView) convertView.findViewById(R.id.item_iv_buybtn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsModel item = (GoodsModel) getItem(position);
        holder.goodsName.setText(item.getGoodsName());
        holder.goodsPic.setImageResource(item.getGoodsPic());
        final ImageView curIv = holder.goodsPic;
        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.addGoodsToCart(curIv);
                }
            }
        });
        holder.goodsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.addGoodsToCart(curIv);
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView goodsName;
        ImageView goodsPic;
        TextView buyBtn;
    }

    public void setCartAddListener(CartAddListener l){
        listener = l;
    }
}
