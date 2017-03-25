package com.scu.lly.customviews.view.slidingview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by lusheep on 2016/6/29.
 * 放在ListView Item布局中的可以侧滑出菜单的布局
 * 注意：1、最好不要直接把SlidingItemLayout作为item布局的根布局，这样会导致高度设定不起作用，像Demo中一样外面先随便套一层其他布局即可；
 * 2、在后面把侧滑菜单布局include进来，因此我们可以更改菜单布局的样式，按钮个数等...
 * 3、SlidingItemLayout里面只能有两个子view，一个是用户自编写的，一个是include进来的侧滑菜单布局。因此，用户在编写自己布局时，先用一个布局大布局包裹起来；
 */
public class SlidingItemLayout extends LinearLayout {

    private Scroller mScroller;
    private int mLastX;
    private int mLastY;

    /**
     * 侧滑菜单布局
     */
    private View mMenuLayout;
    /**
     * 侧滑菜单的宽度
     */
    private int menuWidth = 0;

    private int mTouchSlop = 0;


    /**
     * 定义的一些滑动状态
     */
    public static final int SCROLL_STATE_CLOSE= 1;//关闭
    public static final int SCROLL_STATE_SCROLLING = 2;//滑动状态
    public static final int SCROLL_STATE_OPEN = 3;//完全打开的状态

    /**
     * 速度跟踪器
     */
    private VelocityTracker mVelocityTracker;
    private int mMinVelocityX = 0;

    public SlidingItemLayout(Context context) {
        super(context);
        init(context);
    }

    public SlidingItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlidingItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setOrientation(LinearLayout.HORIZONTAL);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMinVelocityX = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        /**
         * 得到侧滑的菜单布局，因为总共只有两个大布局，直接取index=1的子View
         */
        mMenuLayout = getChildAt(1);
        menuWidth = mMenuLayout.getLayoutParams().width;
    }

    /**
     * 处理ListView中传递过来的触摸事件
     * @param ev
     */
    public void delOnTouchEvent(MotionEvent ev){
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int dy = mLastY - y;
                /**
                 * 判断侧滑条件是否满足，这里的侧滑条件要和ListView中的侧滑条件一致
                 */
                if(!slidingCondition(dx,dy)){//侧滑条件不满足
                    Log.d("TAG", "----SlindIntemLayout--MOVE--侧滑条件不满足-->");
                    break;
                }
                /**
                 * 下面是边界处理
                 */
                if(getScrollX() + dx < 0) {
                    dx  = 0;
                }
                if( getScrollX() + dx > menuWidth){
                    dx = menuWidth - getScrollX();
                }
                scrollBy(dx, 0);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                int velcityX = (int) mVelocityTracker.getXVelocity();
                //手指抬起处理
                scorllUp(velcityX);
                mVelocityTracker.clear();
                break;
        }

        mLastX = x;
        mLastY = y;
    }

    /**
     * 处理手指抬起时的事件，根据速度和已经侧滑的距离决定侧滑的行为
     * @param velcityX
     */
    private void scorllUp(int velcityX) {
        boolean closeFlag4Velcity = velcityX > 0 && Math.abs(velcityX) >= mMinVelocityX;//速度达到，可以关闭侧滑菜单
        boolean openFlag4Velocity = velcityX < 0 && Math.abs(velcityX) >= mMinVelocityX;//速度达到，可以打开侧滑菜单

        boolean closeFlag4Width = getScrollX() < 0.5 * menuWidth;
        boolean openFlag4Width = getScrollX() >= 0.5 * menuWidth;
        if(velcityX > 0){//手指往右滑动，判定是否要完全关闭
            if(closeFlag4Velcity || closeFlag4Width){//需要关闭侧滑菜单
                closeMenu();
            }else{
                openMenu();
            }
        }else{//手指往左滑动，判定是否要完全打开侧滑菜单
            if(openFlag4Velocity || openFlag4Width){
                openMenu();
            }else{
                closeMenu();
            }
        }
    }

    private void openMenu() {
        mScroller.startScroll(getScrollX(),0, menuWidth - getScrollX(), 0);
        invalidate();
    }

    private void closeMenu() {
        mScroller.startScroll(getScrollX(),0,  -getScrollX(), 0);
        invalidate();
    }

    /**
     * 重置到关闭侧滑菜单的状态，
     * 在自定义ListView和Adapter都可能用到
     */
    public void reset(){
        if(getScrollX() != 0){
            closeMenu();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 查询当前的滑动状态
     * @return
     */
    public int getScrollState(){
        int scrollx = getScrollX();
        if(scrollx == 0){
            return SCROLL_STATE_CLOSE;
        }else if(scrollx == menuWidth){
            return SCROLL_STATE_OPEN;
        }else{
            return SCROLL_STATE_SCROLLING;
        }
    }

    /**
     * 判断侧滑条件是否满足
     * @param dx
     * @param dy
     * @return true-表示满足侧滑条件
     */
    public static boolean slidingCondition(int dx, int dy){
        return Math.abs(dx) > Math.abs(dy) * 2;
    }
}
