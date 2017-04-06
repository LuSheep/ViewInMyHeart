package com.scu.lly.customviews.view.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.scu.lly.customviews.R;

/**
 * 卫星菜单，注意布局文件需要match_parent或者足够大，否则菜单散开后不能完全显示出来
 * Created by lusheep on 2017/4/6.
 */

public class MenuAnimActivity extends Activity {

    private ImageView menu1, menu2, menu3, menu4, menu5, menu;

    private boolean isShow;//是否处于打开展示的状态

    private int dx1, dy1;
    private int dx2, dy2;
    private int dx3, dy3;
    private int dx4, dy4;
    private int dx5, dy5;

    private double a = Math.toRadians(90) / 4;//角度(90 / 4)

    private int mRadius = 300;//卫星半径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuanim);

        initView();
    }

    private void initView() {
        menu1 = (ImageView) findViewById(R.id.menu_anim_circle1);
        menu2 = (ImageView) findViewById(R.id.menu_anim_circle2);
        menu3 = (ImageView) findViewById(R.id.menu_anim_circle3);
        menu4 = (ImageView) findViewById(R.id.menu_anim_circle4);
        menu5 = (ImageView) findViewById(R.id.menu_anim_circle5);
        menu = (ImageView) findViewById(R.id.menu_anim);

        menu.setOnClickListener(new MenuOnClickListener(0));
        menu1.setOnClickListener(new MenuOnClickListener(1));
        menu2.setOnClickListener(new MenuOnClickListener(2));
        menu3.setOnClickListener(new MenuOnClickListener(3));
        menu4.setOnClickListener(new MenuOnClickListener(4));
        menu5.setOnClickListener(new MenuOnClickListener(5));

        initAnim();
    }

    private void initAnim() {
        double angle = 0;//第一个菜单与按钮呈水平
        dx1 = -(int) (mRadius * Math.cos(angle));
        dy1 = -(int) (mRadius * Math.sin(angle));
        angle += a;

        dx2 = -(int) (mRadius * Math.cos(angle));
        dy2 = -(int) (mRadius * Math.sin(angle));
        angle += a;

        dx3 = -(int) (mRadius * Math.cos(angle));
        dy3 = -(int) (mRadius * Math.sin(angle));
        angle += a;

        dx4 = -(int) (mRadius * Math.cos(angle));
        dy4 = -(int) (mRadius * Math.sin(angle));
        angle += a;

        dx5 = -(int) (mRadius * Math.cos(angle));
        dy5 = -(int) (mRadius * Math.sin(angle));

    }

    private void donAnimOpen(View view, int translationX, int translationY){
        view.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        );
        set.setDuration(500);
        set.start();
    }

    private void donAnimClose(View view, int translationX, int translationY){
//        view.setVisibility(View.GONE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        );
        set.setDuration(500);
        set.start();
    }

    private class MenuOnClickListener implements View.OnClickListener{
        private int which;

        public MenuOnClickListener(int w){
            which = w;
        }

        @Override
        public void onClick(View v) {
            switch (which){
                case 0://打开/关闭按钮
                    if(!isShow){//菜单处于关闭状态，此时需要打开
                        isShow = true;
                        donAnimOpen(menu1, dx1, dy1);
                        donAnimOpen(menu2, dx2, dy2);
                        donAnimOpen(menu3, dx3, dy3);
                        donAnimOpen(menu4, dx4, dy4);
                        donAnimOpen(menu5, dx5, dy5);

                    }else{//此时需要关闭菜单
                        isShow = false;
                        donAnimClose(menu1, dx1, dy1);
                        donAnimClose(menu2, dx2, dy2);
                        donAnimClose(menu3, dx3, dy3);
                        donAnimClose(menu4, dx4, dy4);
                        donAnimClose(menu5, dx5, dy5);
                    }
                    break;
                case 1:
                    Toast.makeText(MenuAnimActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MenuAnimActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(MenuAnimActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(MenuAnimActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(MenuAnimActivity.this, "" + which, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
