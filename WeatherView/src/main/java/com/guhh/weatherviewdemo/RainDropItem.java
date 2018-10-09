package com.guhh.weatherviewdemo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class RainDropItem {
    private int canvasHeight = 0;
    private int canvasWidth = 0;
    private float speed = 0.5f;
    public int rainOffsetX = 0;
    private int lengthX = 0;
    private int lengthY = 0;
    public int startX = 0;
    public int startY = 0;
    public int stopX = 0;
    public int stopY = 0;
    public Paint paint;
    private Context context;
    private static Random random = new Random(System.currentTimeMillis());
    @SuppressLint("RestrictedApi")
    public RainDropItem(int canvasWidth, int canvasHeight, Context context, int color){
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setAlpha(random.nextInt(155)+100);
        paint.setStrokeWidth(random.nextInt(3)+1);

        this.context = context;
        this.rainOffsetX = rainOffsetX;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        iniRainItemPosition(true);

    }

    //初始化雨点的位置
    private void iniRainItemPosition(boolean isFirstTime){
        startX = random.nextInt(canvasWidth);
        if(isFirstTime)
            startY = random.nextInt(canvasHeight);
        else
            startY = 0;
        stopX = startX + rainOffsetX;
        stopY = startY + random.nextInt(Util.dp2px(context,15))+Util.dp2px(context,10);
        lengthX = stopX - startX;
        lengthY = stopY - startY;
    }

    public void move(){
        startY += speed * lengthY;
        stopY += speed * lengthY;
        startX += speed * lengthX;
        stopX += speed * lengthX;
        if(startY > canvasHeight){
            iniRainItemPosition(false);
        }
    }

    public void draw(Canvas canvas){
    }
}
