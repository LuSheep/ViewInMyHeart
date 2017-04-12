package com.scu.lly.customviews.view.newstudyer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

/**
 * Created by lusheep on 2017/4/10.
 */

public class ChoutiMenuView extends ViewGroup implements View.OnClickListener{

    private boolean isOpen;

    public ChoutiMenuView(Context context) {
        super(context);
    }

    public ChoutiMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChoutiMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        layoutBottom();
        for(int i = 1; i < count; i++){
            View child = getChildAt(i);
            int left = 0;
            int top = getMeasuredHeight() - (i + 1) * child.getMeasuredHeight();
            int right = child.getMeasuredWidth();
            int bottom = getMeasuredHeight() - i * child.getMeasuredHeight();
            child.layout(left, top, right, bottom);
            if(!isOpen){
                child.setVisibility(View.GONE);
            }
        }
    }

    private void layoutBottom() {
        View child = getChildAt(0);
        int cw = child.getMeasuredWidth();
        int ch = child.getMeasuredHeight();
        child.setOnClickListener(this);
        child.layout(0, getMeasuredHeight() - ch, cw, getMeasuredHeight());
    }

    @Override
    public void onClick(View v) {
        toggleMenu();
    }

    private void toggleMenu() {
        if(!isOpen){//此时需要打开
            int count = getChildCount();
            for(int i = 1; i < count; i++){//从i=1开始
                View child = getChildAt(i);
                /*
                TranslateAnimation anim = new TranslateAnimation(-child.getMeasuredWidth(), 0, 0, 0);
                anim.setDuration(1000 + i * 100);
                child.startAnimation(anim);
                child.setVisibility(View.VISIBLE);
                */

                //换种方式
                ObjectAnimator anim = ObjectAnimator.ofFloat(child, "translationX", -child.getMeasuredWidth(), 0);
                anim.setDuration(1000 + i * 100);
                anim.start();
                child.setVisibility(View.VISIBLE);
            }
            isOpen = true;
        }else{//关闭
            int count = getChildCount();
            AnimatorSet set = new AnimatorSet();
            for(int i = 1; i < count; i++) {//从i=1开始
                View child = getChildAt(i);
                /*
                TranslateAnimation anim = new TranslateAnimation(0, -child.getMeasuredWidth(), 0, 0);
                anim.setDuration(1000 + i * 100);
                child.startAnimation(anim);
                child.setVisibility(View.GONE);
                */
                //换种方式
                ObjectAnimator anim = ObjectAnimator.ofFloat(child, "translationX", 0, -child.getMeasuredWidth());
                anim.setDuration(1000 + i * 100);
                set.playTogether(anim);
            }
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    int count = getChildCount();
                    for(int i = 1; i < count; i++){
                        View child = getChildAt(i);
                        child.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
            isOpen = false;
        }
//        View btn = getChildAt(0);
//        ObjectAnimator anim0 = ObjectAnimator.ofFloat(btn, "translationX", 0, btn.getMeasuredWidth(), 0);
//        anim0.setDuration(1200);
//        anim0.start();
    }
}
