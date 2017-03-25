package com.scu.lly.customviews.view.pinnedheaderexpandablelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import java.util.NoSuchElementException;

/**
 * 顶部head可伸缩的自定义布局，只有两个子元素，一个header，一个content
 * Created by lly on 2017/3/23.
 */

public class StickyLayout extends LinearLayout {

    private int mLastInterceptX;
    private int mLastInterceptY;
    private int mLastX;
    private int mLastY;

    private static final int STAUS_EXPANDED = 1;
    private static final int STAUS_COLLAPSED = 2;

    private int mStatus = STAUS_EXPANDED;

    private int mHeaderHeight;
    private int mOrigHeaderHeight;

    private View mHeaderView;
    private View mContentView;

    private boolean mIsInitDataSuccess = false;
    private boolean mDisallowInterceptTouchEventOnHeader = true;
    private boolean mIsSticky = true;

    private int mTouchSlop;

    public interface OnGiveUpTouchEventListener{
        public boolean giveUpTouchEvent(MotionEvent ev);
    }

    private OnGiveUpTouchEventListener mOnGiveUpTouchEventListener;

    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l){
        mOnGiveUpTouchEventListener = l;
    }

    public StickyLayout(Context context) {
        super(context);
    }

    public StickyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasWindowFocus && (mHeaderView == null || mContentView == null)){
            initData();
        }
    }

    private void initData() {
        int headerId = getResources().getIdentifier("sticky_header", "id", getContext().getPackageName());
        int contentId = getResources().getIdentifier("sticky_content", "id", getContext().getPackageName());
        if(headerId != 0 && contentId != 0){
            mHeaderView = findViewById(headerId);
            mContentView = findViewById(contentId);
            mOrigHeaderHeight = mHeaderView.getMeasuredHeight();
            mHeaderHeight = mOrigHeaderHeight;
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            if(mHeaderHeight > 0){
                mIsInitDataSuccess = true;
            }
        }else{
            throw new NoSuchElementException("Did your view with id \\\"sticky_header\\\" or \\\"sticky_content\\\" exists?");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int intercept = 0;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercept = 0;
                mLastInterceptX = x;
                mLastInterceptY = y;
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;
                if(mDisallowInterceptTouchEventOnHeader && y <= mHeaderHeight){
                    intercept = 0;
                }else if(Math.abs(deltaY) <= Math.abs(deltaX)){
                    intercept = 0;
                }else if(mStatus == STAUS_EXPANDED && deltaY <= -mTouchSlop){
                    intercept = 1;
                }else if(mOnGiveUpTouchEventListener != null){
                    if(mOnGiveUpTouchEventListener.giveUpTouchEvent(ev) && deltaY >= mTouchSlop){
                        intercept = 1;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = 0;
                mLastInterceptX = mLastInterceptY = 0;
                break;
        }

        return intercept != 0 && mIsSticky;
    }

    public void setSticky(boolean isSticky) {
        mIsSticky = isSticky;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!mIsSticky){
            return true;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                mHeaderHeight += deltaY;
                setHeaderHeight(mHeaderHeight);
                break;
            case MotionEvent.ACTION_UP:
                int desHeight = 0;
                if(mHeaderHeight <= mOrigHeaderHeight * 0.5){
                    desHeight = 0;
                    mStatus = STAUS_COLLAPSED;
                }else{
                    desHeight = mOrigHeaderHeight;
                    mStatus = STAUS_EXPANDED;
                }
                smoothSetHeaderHeight(mHeaderHeight, desHeight, 500);
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothSetHeaderHeight(int from, int to, int duration) {
        smoothSetHeaderHeight(from, to, duration, false);
    }

    private void smoothSetHeaderHeight(final int from, final int to, int duration, final boolean modifyOriginalHeaderHeight) {
        final int frameCount = (int) ((duration / 1000f * 30)) + 1;
        final float partation = (to - from) / (float)frameCount;
        new Thread("Thread#smoothSetHeaderHeight"){
            @Override
            public void run() {
                for(int i = 0; i < frameCount; i++){
                    final int height;
                    if(i == frameCount - 1){
                        height = to;
                    }else{
                        height = (int) (from + partation * i);
                    }
                    post(new Runnable() {
                        @Override
                        public void run() {
                            setHeaderHeight(height);
                        }
                    });
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (modifyOriginalHeaderHeight) {
                    setOriginalHeaderHeight(to);
                }
            }
        }.start();
    }

    public void setOriginalHeaderHeight(int originalHeaderHeight) {
        mOrigHeaderHeight = originalHeaderHeight;
    }

    private void setHeaderHeight(int height) {
        if(!mIsInitDataSuccess){
            initData();
        }
        if(height <= 0){
            height = 0;
        }else if(height > mOrigHeaderHeight){
            height = mOrigHeaderHeight;
        }
        if(height == 0){
            mStatus = STAUS_COLLAPSED;
        }else{
            mStatus = STAUS_EXPANDED;
        }
        if(mHeaderView != null && mHeaderView.getLayoutParams() != null){
            mHeaderView.getLayoutParams().height = height;
            mHeaderView.requestLayout();
            mHeaderHeight = height;
        }
    }
}
