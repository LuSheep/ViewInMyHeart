package com.scu.lly.customviews.view.tagflow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scu.lly.customviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * （推荐试用版）
 * 加强版的动态浮动标签：当一行的最后一个标签距离右边较远时，自动调整该行的间距
 * Created by lusheep on 2017/3/23.
 */
public class TagFlowEnhanceView extends ViewGroup {

    private List<String> mTags = new ArrayList<String>();

    /**
     * 记录一行中的标签View
     */
    private List<View> mTvList = new ArrayList<View>();

    /**
     * 自定义属性，由布局中指定；
     * 是否需要动态处理一行的间距，默认true表示需要（防止最后一个元素间距过大）
     */
    private boolean needDynamicDealMargin = true;

    /**
     * 自定义属性，由布局中指定；只有needDynamicDealMargin=true了这个属性才有意义
     * 一行中，在动态分配间距时（即needDynamicDealMargin = true），最左边和最右边的两个标签View是否分配margin。
     * 值为false，表示不分配，此时所有标签左右两边会对齐。为true，两边不会对齐
     */
    private boolean isEdgeLeftAndRightViewAssignMargin = true;

    public TagFlowEnhanceView(Context context) {
        super(context);
    }

    public TagFlowEnhanceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowEnhanceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowEnhanceView);
        needDynamicDealMargin = ta.getBoolean(R.styleable.TagFlowEnhanceView_needDynamicDealMargin, true);
        isEdgeLeftAndRightViewAssignMargin = ta.getBoolean(
                R.styleable.TagFlowEnhanceView_isEdgeLeftAndRightViewAssignMargin, true);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //下面处理的是宽或者高为wrap_content的情况...

        int curLineTotalWidth = 0;//记录当前这一行的总宽度
        int maxLineWidth = 0;//所有行中宽度最大值
        int curLineMaxHeight = 0;//记录当前这一行中的最大高度值
        int totalHeight = 0;//总高度

        MarginLayoutParams lp;
        mTvList.clear();

        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if(curLineTotalWidth + childWidth > widthSpecSize){//加上新的标签后会超出这一行，此时，需要另起一行
                if(maxLineWidth < curLineTotalWidth){
                    maxLineWidth = curLineTotalWidth;
                }

                dealDistributionMargin(widthSpecMode, widthSpecSize, curLineTotalWidth, mTvList);

                mTvList.clear();
                mTvList.add(child);

                totalHeight += curLineMaxHeight;
                curLineTotalWidth = childWidth;
                curLineMaxHeight = childHeight;//新的一行第一个

                if(i == childCount - 1){//最后一个元素
                    totalHeight += curLineMaxHeight;
                    if(maxLineWidth < curLineTotalWidth){
                        maxLineWidth = curLineTotalWidth;
                    }
                }
            }else{//当前行还可以放下新元素
                mTvList.add(child);
                curLineTotalWidth += childWidth;
                if(maxLineWidth < curLineTotalWidth){
                    maxLineWidth = curLineTotalWidth;
                }
                if(curLineMaxHeight < childHeight){
                    curLineMaxHeight = childHeight;
                }
                if(i == childCount - 1){//最后一个元素
                    totalHeight += curLineMaxHeight;
                    dealDistributionMargin(widthSpecMode, widthSpecSize, curLineTotalWidth, mTvList);
                }
            }
        }
        setMeasuredDimension(widthSpecMode == MeasureSpec.EXACTLY ? widthSpecSize : maxLineWidth,
                heightSpecMode == MeasureSpec.EXACTLY ? heightSpecSize : totalHeight);
    }

    /**
     * 动态分配一行的间距
     * @param widthSpecMode
     * @param widthSpecSize
     * @param curLineTotalWidth
     * @param dealViews
     */
    private void dealDistributionMargin(int widthSpecMode, int widthSpecSize,
                                        int curLineTotalWidth,List<View> dealViews){
        if(!needDynamicDealMargin)
            return;
        if(widthSpecMode == MeasureSpec.EXACTLY && dealViews.size() > 1){
            int leftWidthSpace = widthSpecSize - curLineTotalWidth;
            int marginCount;
            if(isEdgeLeftAndRightViewAssignMargin){//最边上的两个标签也需要分配margin
                marginCount = dealViews.size() * 2;//
            }else{
                marginCount = (dealViews.size() - 1) * 2;//一个标签有左右两个margin，减去最左、最右的两个margin
            }
            if(leftWidthSpace >= marginCount){//对这一行的标签View重新设置margin
                int addMargin = leftWidthSpace / marginCount;
                for(int k = 0; k < dealViews.size(); k++){
                    View v = dealViews.get(k);
                    MarginLayoutParams vl = (MarginLayoutParams) v.getLayoutParams();
                    if(isEdgeLeftAndRightViewAssignMargin) {//最边上的两个标签也需要分配margin
                        vl.leftMargin += addMargin;
                        vl.rightMargin += addMargin;
                    }else{
                        if(k == 0){
                            vl.rightMargin += addMargin;
                        }else if(k == dealViews.size() - 1){
                            vl.leftMargin += addMargin;
                        }else{
                            vl.leftMargin += addMargin;
                            vl.rightMargin += addMargin;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();

        int curLineTotalWidth = 0;//记录当前这一行的总宽度
        int curLineMaxHeight = 0;//记录当前这一行中的最大高度值
        int totalHeight = 0;//总高度

        MarginLayoutParams lp;

        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = getChildAt(i);
            lp = (MarginLayoutParams) child.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if(curLineTotalWidth + childWidth > width) {//加上新的标签后会超出这一行，此时，需要另起一行
                totalHeight += curLineMaxHeight;
                curLineTotalWidth = childWidth;
                curLineMaxHeight = childHeight;
                cl = lp.leftMargin;
                ct = totalHeight + lp.topMargin;
            }else{
                cl = curLineTotalWidth + lp.leftMargin;
                ct = totalHeight + lp.topMargin;
                curLineTotalWidth += childWidth;
                if(curLineMaxHeight < childHeight){
                    curLineMaxHeight = childHeight;
                }
            }
            cr = cl + child.getMeasuredWidth();
            cb = ct + child.getMeasuredHeight();

            child.layout(cl, ct, cr, cb);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    public void setTags(List<String> tags){
        if(tags != null){
            mTags.clear();
            mTags.addAll(tags);

            for(int i = 0; i <mTags.size(); i++){
                TextView tv = new TextView(getContext());
                MarginLayoutParams lp = (MarginLayoutParams) generateLayoutParams(
                        new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                lp.setMargins(15, 15, 15, 15);

                tv.setLayoutParams(lp);
                tv.setBackgroundResource(R.drawable.tv_bg);

                tv.setPadding(15, 15, 15, 15);
                tv.setTextColor(Color.WHITE);

                tv.setText(mTags.get(i));

                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            listener.onClick(v);
                        }
                    }
                });

                addView(tv);
            }

            requestLayout();
        }
    }

    private OnTagItemClickListener listener;
    public interface OnTagItemClickListener{
        public void onClick(View v);
    }
    public void setOnTagItemClickListener(OnTagItemClickListener l){
        listener = l;
    }


}
