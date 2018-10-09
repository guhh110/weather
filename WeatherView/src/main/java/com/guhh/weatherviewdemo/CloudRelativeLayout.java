package com.guhh.weatherviewdemo;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class CloudRelativeLayout extends RelativeLayout {
    private ArrayList<ImageView> cloudIvs = new ArrayList<>();
    private int[] cloudImgs = new int[]{R.drawable.ic_cloud1,R.drawable.ic_cloud2,R.drawable.ic_cloud3};
    private int cloudCount = 10;
    private int sunHeight =0;
    private int sunWidth = 0;
    private int height = 0;
    private int width = 0;
    private int cloudMaxSpeed = 3000;
    private int cloudMinSpeed = 8000;
    private int cloudMaxSize = 150;
    private int cloudMinSize = 50;
    private int cloudStartPosition = 0;
    private int cloudEndPosition = 30;
    private boolean isFirstTime = true;
    public CloudRelativeLayout(Context context) {
        super(context);
    }
    public CloudRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CloudRelativeLayout);
        cloudCount = typedArray.getInt(R.styleable.CloudRelativeLayout_cloudCount,0);
        sunHeight = (int) typedArray.getDimension(R.styleable.CloudRelativeLayout_sunHeight,Util.dp2px(context,0));
        sunWidth = (int) typedArray.getDimension(R.styleable.CloudRelativeLayout_sunWidth,Util.dp2px(context,0));
        cloudMinSize =(int)  typedArray.getDimension(R.styleable.CloudRelativeLayout_cloudMinSize,Util.dp2px(context,0));
        cloudMaxSize = (int) typedArray.getDimension(R.styleable.CloudRelativeLayout_cloudMaxSize,Util.dp2px(context,0));
        cloudEndPosition = (int) typedArray.getDimension(R.styleable.CloudRelativeLayout_cloudEndPosition,Util.dp2px(context,0));
        cloudStartPosition = (int) typedArray.getDimension(R.styleable.CloudRelativeLayout_cloudStartPosition,Util.dp2px(context,0));
        cloudMinSpeed = typedArray.getInt(R.styleable.CloudRelativeLayout_cloudMinSpeed,3) * 1000;
        cloudMaxSpeed = typedArray.getInt(R.styleable.CloudRelativeLayout_cloudMaxSpeed,5) * 1000;
        typedArray.recycle();

        //添加一个太阳
        ImageView sunIv = new ImageView(context);
        sunIv.setImageResource(R.drawable.ic_sun);
        RelativeLayout.LayoutParams layoutParamsSun = new RelativeLayout.LayoutParams(sunHeight, sunWidth);
        this.addView(sunIv,layoutParamsSun);
        //添加云
        for(int i = 0;i<cloudCount;i++){
            Random random = new Random();
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(cloudImgs[random.nextInt(3)]);
            int cloudSize = random.nextInt(cloudMaxSize-cloudMinSize)+cloudMinSize;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(cloudSize,cloudSize);
            this.addView(imageView,layoutParams);
            cloudIvs.add(imageView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(isFirstTime){
            height = getHeight();
            width = getWidth();
            Log.i("sssddd",getHeight()+"-"+getWidth());
            Random random = new Random(System.currentTimeMillis());
            //为云添加动画
            for(ImageView iv : cloudIvs){
                int leftMargin = random.nextInt(width-cloudMinSize);
                int heightMargin = random.nextInt(cloudEndPosition - cloudStartPosition)+cloudStartPosition;
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) iv.getLayoutParams();
                layoutParams.setMargins(leftMargin,heightMargin,0,0);
                iv.setLayoutParams(layoutParams);
                Path path = new Path();
                path.moveTo(iv.getX(),iv.getY());
                path.lineTo(width-leftMargin,iv.getY());
                path.moveTo(-leftMargin-iv.getWidth(),iv.getY());
                path.lineTo(iv.getX(),iv.getY());
                ObjectAnimator animator = ObjectAnimator.ofFloat(iv,"translationX","translationY",path);
                animator.setDuration(random.nextInt(cloudMaxSpeed-cloudMinSpeed)+cloudMinSpeed);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
            }
            isFirstTime = false;
        }

    }
}
