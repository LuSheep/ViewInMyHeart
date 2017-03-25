package com.scu.lly.customviews.view.slidingview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

import com.scu.lly.customviews.model.SlidingItemModel;

/**
 * Created by lusheep on 2016/6/29.
 */
public class SlidingListView extends ListView{

    private SlidingItemLayout curMovingItem;

    private int mLastX;
    private int mLastY;
    private int mLastPosition;

    public SlidingListView(Context context) {
        super(context);
    }

    public SlidingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:

                int whichPosition = pointToPosition(x, y);

                /**
                 *表示如果在本次ACTION_DOWN事件时，之前有item已经滑动出来了，
                 * 并且本次点击的位置不是之前滑动出来条目的位置，此时就需要将滑动出来的条目关闭
                 */
                if(whichPosition != mLastPosition && curMovingItem != null && (curMovingItem.getScrollState() != SlidingItemLayout.SCROLL_STATE_CLOSE) ){
                    curMovingItem.reset();
                }

                if (whichPosition != INVALID_POSITION) {
                    SlidingItemModel model = (SlidingItemModel) getItemAtPosition(whichPosition);
                    curMovingItem = model.menuLayout;
                    mLastPosition = whichPosition;
                }
                 break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int dy = mLastY - y;
                /**
                 * 判断侧滑条件是否满足，这里的侧滑条件要和SlidingItemLayout中的侧滑条件一致
                 */
                if (SlidingItemLayout.slidingCondition(dx,dy)) {
                    if (curMovingItem != null) {
                        curMovingItem.delOnTouchEvent(ev);//往侧滑布局分发事件
                    }
                    //滑动事件不再交给listview去处理，直接消耗掉
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (curMovingItem != null && curMovingItem.getScrollState() == SlidingItemLayout.SCROLL_STATE_SCROLLING) {
                    Log.d("TAG","----listview--ACTION_UP---->");
                    curMovingItem.delOnTouchEvent(ev);
                    /**
                     * 现在有个矛盾就是，我们现在return true了，这样可以防止在侧滑的时候，listview的item的点击事件被触发，
                     * 但是当listview如果设置了selector时，抬起时还是被选中的一种状态，也就是没有了抬起状态
                     */
                    return true;
                }
                break;
         }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(ev);
    }
}
