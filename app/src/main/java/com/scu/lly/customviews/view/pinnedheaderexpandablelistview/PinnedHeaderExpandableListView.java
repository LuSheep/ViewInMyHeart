package com.scu.lly.customviews.view.pinnedheaderexpandablelistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

/**
 * 滑动时固定头部的ExpandableListView
 * Created by 廖露阳 on 2017/3/22.
 */

public class PinnedHeaderExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener{

    private OnScrollListener mOnScrollListener;
    private View mHeaderView;
    private int mHeaderWidth;
    private int mHeaderHeight;

    private View mTouchTargetView;

    private boolean mActionDownHappened = false;
    private boolean mIsHeaderGroupClickable = true;

    public interface OnHeaderUpdateListener{
        public View getPinnedHeaderView();
        public void updatePinnedHeaderView(View headerView, int firstVisibleGroupPos);
    }

    private OnHeaderUpdateListener mOnHeaderUpdateListener;


    public PinnedHeaderExpandableListView(Context context) {
        super(context);
        init();
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFadingEdgeLength(0);
        setOnScrollListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mHeaderView != null){
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderWidth = mHeaderView.getMeasuredWidth();
            mHeaderHeight = mHeaderView.getMeasuredHeight();
        }else{
            mHeaderWidth = 0;
            mHeaderHeight = 0;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(mHeaderView != null){
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mHeaderView != null){
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //处理点击到固定的HeaderView时的情形
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int pos = pointToPosition(x, y);
        if(mHeaderView != null && y >= mHeaderView.getTop() && y <= mHeaderView.getBottom()){
            if(ev.getAction() == MotionEvent.ACTION_DOWN){
                mTouchTargetView = getTouchTargetView(mHeaderView, x, y);
                mActionDownHappened = true;
            }else if(ev.getAction() == MotionEvent.ACTION_UP){
                View touchTarget = getTouchTargetView(mHeaderView, x, y);
                if(touchTarget == mTouchTargetView && mTouchTargetView.isClickable()){
                    mTouchTargetView.performClick();
                    invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
                }else if(mIsHeaderGroupClickable){
                    int groupPos = getPackedPositionGroup(getExpandableListPosition(pos));
                    if(groupPos != INVALID_POSITION && mActionDownHappened){
                        if(isGroupExpanded(groupPos)){
                            collapseGroup(groupPos);
                        }else{
                            expandGroup(groupPos);
                        }
                    }
                }
                mActionDownHappened = false;
            }
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断点击事件具体落在View中的哪个控件上
     * @param view
     * @param x
     * @param y
     * @return
     */
    private View getTouchTargetView(View view, int x, int y) {
        if(!(view instanceof ViewGroup)){
            return view;
        }
        ViewGroup parent = (ViewGroup) view;
        int childCount = parent.getChildCount();
        boolean customOrder = isChildrenDrawingOrderEnabled();
        View target = null;
        for(int i = childCount - 1; i >= 0; i--){
            int childIndex = customOrder ? getChildDrawingOrder(childCount, i) : i;
            View child = parent.getChildAt(childIndex);
            if(isTouchPointInView(child, x, y)){
                target = child;
                break;
            }
        }
        if(target == null){
            target = parent;
        }
        return target;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if(view.isClickable() && x >= view.getLeft() && x <= view.getRight()
                && y >= view.getTop() && y <= view.getBottom()){
            return true;
        }
        return false;
    }


    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if(l != this){
            mOnScrollListener = l;
        }else{
            mOnScrollListener = null;
        }
        super.setOnScrollListener(this);
    }

    public void setOnHeaderUpdateListener(OnHeaderUpdateListener l){
        if(l != null){
            mOnHeaderUpdateListener = l;
            mHeaderView = l.getPinnedHeaderView();

            int firstVisiblePos = getFirstVisiblePosition();
            int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
            l.updatePinnedHeaderView(mHeaderView, firstVisibleGroupPos);
            requestLayout();
            postInvalidate();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mOnScrollListener != null){
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount > 0){
            refreshHeader();
        }

        if(mOnScrollListener != null){
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void refreshHeader() {
        if(mHeaderView == null)
            return;
        //比较可见视图中最前面的两条item是否是属于同一个group（group item也属于一种item）
        int firstVisiblePos = getFirstVisiblePosition();
        int pos = firstVisiblePos + 1;
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        int nextItemGroupPos = getPackedPositionGroup(getExpandableListPosition(pos));

        if(nextItemGroupPos == firstVisibleGroupPos + 1){//说明最前面的两条item属于不同的group
            View view = getChildAt(1);
            if(view == null){
                return;
            }
            if(view.getTop() <= mHeaderHeight){
                int deltaY = mHeaderHeight - view.getTop();
                mHeaderView.layout(0, -deltaY, mHeaderWidth, mHeaderHeight - deltaY);
            }else{
                mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
            }
        }else{
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }

        if(mOnHeaderUpdateListener != null){
            mOnHeaderUpdateListener.updatePinnedHeaderView(mHeaderView, firstVisibleGroupPos);
        }
    }
}
