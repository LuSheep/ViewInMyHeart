package com.scu.lly.customviews.view.tagflow;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scu.lly.customviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * （推荐使用加强版：TagFlowEnhanceView）
 * 动态浮动标签
 * Created by lusheep on 2017/3/23.
 */
public class TagFlowView extends ViewGroup {

    private List<String> mTags = new ArrayList<String>();

    public TagFlowView(Context context) {
        super(context);
    }

    public TagFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                curLineTotalWidth += childWidth;
                if(maxLineWidth < curLineTotalWidth){
                    maxLineWidth = curLineTotalWidth;
                }
                if(curLineMaxHeight < childHeight){
                    curLineMaxHeight = childHeight;
                }
                if(i == childCount - 1){//最后一个元素
                    totalHeight += curLineMaxHeight;
                }
            }
        }
        setMeasuredDimension(widthSpecMode == MeasureSpec.EXACTLY ? widthSpecSize : maxLineWidth,
                heightSpecMode == MeasureSpec.EXACTLY ? heightSpecSize : totalHeight);
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
