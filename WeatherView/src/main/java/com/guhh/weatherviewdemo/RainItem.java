package com.guhh.weatherviewdemo;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class RainItem {

    private int width;
    private int height;

    public RainItem(int width, int height) {
        this.width = width;
        this.height = height;
        init();
    }

    public float startX = 0;
    public float startY = 0; // Start Point
    public float stopX = 0;
    public float stopY = 0; // Stop Point
    private float sizeX = 0;
    private float sizeY = 0;
    private Paint paint;
    private float opt;
    private Random random;

    public void init() {
        // TODO Auto-generated method stub
        random = new Random();

        sizeX = 5 + random.nextInt(10); // 随机改变雨点的角度
        sizeY = 5 + random.nextInt(10);
        // 单个雨点的形状，用线段表示
        startX = random.nextInt(this.width);
        startY = random.nextInt(this.height); // 随机改变雨点的位置
        stopX = startX + sizeX;
        stopY = startY + sizeY;

        paint = new Paint();
        paint.setColor(0xffffffff);
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    public void move() {
        // 让雨点运动
        opt = 1f; // 随机改变雨点的速度和长度
        startX += sizeX * opt;
        stopX += sizeX * opt;
        startY += sizeY * opt;
        stopY += sizeY * opt;

        // 雨点出了屏幕的时候让它回到起始点
        if (startY > this.height || startX > this.width) {
            startX = random.nextInt(this.width);
            startY = random.nextInt(this.height);
            stopX = startX + sizeX;
            stopY = startY + sizeY;
        }
    }

}