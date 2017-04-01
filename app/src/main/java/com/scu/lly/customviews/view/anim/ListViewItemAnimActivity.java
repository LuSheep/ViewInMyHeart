package com.scu.lly.customviews.view.anim;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.model.GoodsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView item的进场动画
 * Created by lusheep on 2017/3/30.
 */

public class ListViewItemAnimActivity extends Activity {

    private ListView mListView;
    private List<GoodsModel> mDatas = new ArrayList<GoodsModel>();
    private GoodsAdapter mAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewanim);

        mListView = (ListView) findViewById(R.id.listview_anim);

        initData();
    }

    private void initData() {
        GoodsModel good1 = new GoodsModel("蛋糕",R.drawable.cake);
        mDatas.add(good1);

        GoodsModel good2 = new GoodsModel("咖啡",R.drawable.coffee);
        mDatas.add(good2);

        GoodsModel good3 = new GoodsModel("茶壶",R.drawable.kettle);
        mDatas.add(good3);

        GoodsModel good4 = new GoodsModel("牛奶",R.drawable.milk);
        mDatas.add(good4);

        GoodsModel good5 = new GoodsModel("手机",R.drawable.mobile);
        mDatas.add(good5);

        mAdapter = new GoodsAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    class GoodsAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater mLayoutInflater;

        public GoodsAdapter(Context ct){
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

            return convertView;
        }

        class ViewHolder{
            TextView goodsName;
            ImageView goodsPic;
            TextView buyBtn;
        }
    }
}
