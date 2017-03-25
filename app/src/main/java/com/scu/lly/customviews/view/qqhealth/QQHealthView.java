package com.scu.lly.customviews.view.qqhealth;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.scu.lly.customviews.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lusheep on 2016/7/5.
 */
public class QQHealthView extends View{
    private Paint mBgPaint;//绘制背景画笔
    private Path mBgPath;

    private Paint mNormalTextPaint;//绘制普通文字画笔
    private Paint mArcPaint;//绘制圆弧画笔

    private int textColor;//文字颜色
    private int lineColor;//画线的颜色

    private Paint dashPaint;//虚线画笔
    private Path dashPath;

    private Paint dayPaint;
    private Path dayPath;

    private Paint mWeavePaint;//底部水底波纹画笔
    private Path mWeavePath;

    private int mRadius;//顶部圆角半径
    private int mWidth;
    private int mHeight;

    private RectF rectF;//绘制圆弧所处的矩阵

    private int walkNum = 20;//已走的步数
    private int rank = 111;//排名
    private int avgWalkNum = 300;//平均步数
    private float arcNum;//圆弧扫过的角度

    private String havingWalkNumText = "截止15:36已走";
    private String friendAvgWalkNumText = "好友平均120步";

    private DashPathEffect dashPathEffect;

    private List<Integer> daysData = new ArrayList<Integer>();

    private AnimatorSet animatorSet;
    private Random random;

    public QQHealthView(Context context) {
        this(context, null);
    }

    public QQHealthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQHealthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //拿到自定义的属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QQHealthViewStyles);
        textColor = ta.getColor(R.styleable.QQHealthViewStyles_titleColor, Color.BLACK);
        lineColor = ta.getColor(R.styleable.QQHealthViewStyles_lineColor, Color.BLUE);
        ta.recycle();

        initData();
    }

    private void initData() {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPath = new Path();

        mNormalTextPaint = new Paint();
        mNormalTextPaint.setAntiAlias(true);
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);

        dashPaint = new Paint();
        dashPaint.setAntiAlias(true);
        dashPath = new Path();

        dayPaint = new Paint();
        dayPaint.setAntiAlias(true);
        dayPath = new Path();

        mWeavePaint = new Paint();
        mWeavePaint.setAntiAlias(true);
        mWeavePath = new Path();

        animatorSet = new AnimatorSet();
        random = new Random();

        dashPathEffect = new DashPathEffect(new float[]{5,5} , 1);

        daysData.add(80);
        daysData.add(100);
        daysData.add(330);
        daysData.add(160);
        daysData.add(200);
        daysData.add(180);
        daysData.add(360);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = widthSize / 2;
        }
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = heightSize * 3 / 4;
        }
        mWidth = widthSize;
        mHeight = heightSize;
        setMeasuredDimension(widthSize,heightSize);

        startAnim();//开启动画
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先绘制底部背景
        mRadius = mWidth / 20;
        mBgPath.moveTo(0, mHeight);
        mBgPath.lineTo(0, mRadius);
        mBgPath.quadTo(0, 0, mRadius, 0);
        mBgPath.lineTo(mWidth - mRadius, 0);
        mBgPath.quadTo(mWidth, 0, mWidth, mRadius);
        mBgPath.lineTo(mWidth, mHeight);
        mBgPath.lineTo(0, mHeight);
        mBgPaint.setColor(Color.WHITE);
        canvas.drawPath(mBgPath,mBgPaint);

        //绘制背景大圆弧
        rectF = new RectF(mWidth / 4, mWidth / 4, mWidth * 3 / 4, mWidth * 3 / 4);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mWidth / 20);
        mArcPaint.setColor(textColor);
        mArcPaint.setDither(true);
        //连接处为圆弧
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);
        //画笔的笔触为圆角
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        //绘制背景大圆弧
        canvas.drawArc(rectF, 120, 300, false, mArcPaint);
        mArcPaint.setColor(lineColor);
        canvas.drawArc(rectF, 120, arcNum, false, mArcPaint);

        //绘制所走步数
        mNormalTextPaint.setColor(lineColor);
        mNormalTextPaint.setTextSize(mWidth / 10);
        int textWidth = (int) mNormalTextPaint.measureText(String.valueOf(walkNum));
        canvas.drawText(String.valueOf(walkNum), mWidth / 2 - textWidth / 2, mWidth / 2 + 20, mNormalTextPaint);
        //绘制名次
        mNormalTextPaint.setTextSize(mWidth / 15);
        int rankTextWidth = (int) mNormalTextPaint.measureText(String.valueOf(rank));
        canvas.drawText(String.valueOf(rank), mWidth / 2 - rankTextWidth / 2, mWidth * 3 / 4 + 10, mNormalTextPaint);

        //绘制其他文字
        mNormalTextPaint.setTextSize(mWidth / 25);
        mNormalTextPaint.setColor(textColor);
        int havingWalkTextWidth = (int) mNormalTextPaint.measureText(havingWalkNumText);
        canvas.drawText(havingWalkNumText, mWidth / 2 - havingWalkTextWidth / 2, mWidth * 3 / 8, mNormalTextPaint);

        int friendAvgWalkTextWidth = (int) mNormalTextPaint.measureText(friendAvgWalkNumText);
        canvas.drawText(friendAvgWalkNumText, mWidth / 2 - friendAvgWalkTextWidth / 2, mWidth * 5 / 8 + 10 , mNormalTextPaint);

        int rankPreTextWidth = (int) mNormalTextPaint.measureText("第");
        int x1 = mWidth / 2 - rankTextWidth / 2 - 15 - rankPreTextWidth / 2;
        int x2 = mWidth / 2 + rankTextWidth / 2 + 5;
        canvas.drawText("第", x1, mWidth * 3 / 4 + 5 , mNormalTextPaint);
        canvas.drawText("名", x2, mWidth * 3 / 4 + 5, mNormalTextPaint);

        //绘制圈外文本
        canvas.drawText("最近7天", mWidth / 15, mWidth , mNormalTextPaint);
        canvas.drawText("平均", mWidth * 10 / 15 - 15, mWidth , mNormalTextPaint);
        canvas.drawText(String.valueOf(avgWalkNum), mWidth * 11 / 15, mWidth, mNormalTextPaint);
        canvas.drawText("步/天", mWidth * 12 / 15 + 20, mWidth, mNormalTextPaint);

        //绘制虚线
        dashPaint.setColor(textColor);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(2);
        dashPath.moveTo(mWidth / 15, mWidth + 80);
        dashPath.lineTo(mWidth * 14 / 15, mWidth + 80);
        dashPaint.setPathEffect(dashPathEffect);
        canvas.drawPath(dashPath,dashPaint);

        //绘制圆角竖线
        int left = mWidth / 12;
        int rectHeight = mWidth / 10;
        int bottomHeight = mWidth + 90 + rectHeight;
        dayPaint.setColor(lineColor);
        dayPaint.setStyle(Paint.Style.STROKE);
        dayPaint.setStrokeWidth(mWidth / 25);
        dayPaint.setStrokeJoin(Paint.Join.ROUND);
        dayPaint.setStrokeCap(Paint.Cap.ROUND);
        double percentage ;
        double height;
        for(int i = 0; i < daysData.size(); i++){
            dayPath.moveTo(left,bottomHeight);
            percentage = Double.valueOf(daysData.get(i)) / Double.valueOf(avgWalkNum);
            height = percentage * rectHeight;
            dayPath.lineTo(left,(float)(bottomHeight - height));
            canvas.drawPath(dayPath,dayPaint);
            //绘制下方的文字
            canvas.drawText("0" + (i + 1) + "日", left - 25, bottomHeight + 50, mNormalTextPaint);
            left += mWidth / 7;
        }

        //绘制底部波纹
        mWeavePaint.setColor(lineColor);
        mWeavePath.moveTo(0, mHeight);
        mWeavePath.lineTo(0, mHeight * 10 / 12);
        mWeavePath.cubicTo(mWidth / 10 + random.nextInt(30), mHeight * 10 / 12 + random.nextInt(80), mWidth * 3 / 10 + random.nextInt(30),
                mHeight * 11 / 12 + random.nextInt(80), mWidth, mHeight * 10 / 12);
        mWeavePath.lineTo(mWidth, mHeight);
        mWeavePath.lineTo(0, mHeight);

        canvas.drawPath(mWeavePath, mWeavePaint);

        //绘制底部文字
        mWeavePaint.setColor(Color.WHITE);
        mWeavePaint.setTextSize(mWidth / 20);
        canvas.drawText("成绩不错,继续努力哟!", mWidth * 1 / 10 - 20, mHeight * 11 / 12 + 50, mWeavePaint);
    }

    private void startAnim(){
        int myWalk = 260;
        ValueAnimator walkAnimator = ValueAnimator.ofInt(0, myWalk);//260为用户当前传过来的步数
        walkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                walkNum = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator rankAnimator = ValueAnimator.ofInt(0, 15);//15为用户传过来的当前排名
        rankAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rank = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        float mywalkFloat = myWalk;
        if(mywalkFloat > avgWalkNum){
            mywalkFloat = avgWalkNum;
        }
        //圆弧动画
        ValueAnimator arcAnimator = ValueAnimator.ofFloat(0, (mywalkFloat / avgWalkNum) * 300);//其中300是上面圆弧扫过的总角度
        arcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcNum = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animatorSet.setDuration(3000);
        animatorSet.playTogether(walkAnimator, rankAnimator, arcAnimator);
        animatorSet.start();
    }
}
