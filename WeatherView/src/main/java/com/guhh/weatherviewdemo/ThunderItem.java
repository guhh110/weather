package com.guhh.weatherviewdemo;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.Random;

public class ThunderItem {
    public Path path;
    public Paint paint;
    private int canvasHeight;
    private int canvasWidth;
    private Context context;
    private int alpha = 255;
    private int speed = 10;
    private static Random random = new Random();
    public ThunderItem(int canvasWidth, int canvasHeight, Context context, int color){
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.context = context;

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(color);
        initThunder();

    }

    private void initThunder(){
        path = new Path();
        alpha = random.nextInt(105)+150;
        paint.setAlpha(alpha);
        paint.setStrokeWidth(random.nextInt(5)+3);
        int startX = random.nextInt(canvasWidth);
        int startY = 0;
        path.moveTo(startX,startY);
        int pointCount = random.nextInt(2)+3;
        for(int i = 0; i<pointCount;i++){
            startX = startX +( 100 - random.nextInt(200));
            startY = startY + random.nextInt(100)+50;
            path.lineTo(startX,startY);
            Log.i("sssddd-ttt",startX+"--"+startY);
        }

    }

    public void thunderFade(){
        alpha -= speed;
        paint.setAlpha(alpha<=0?0:alpha);
        if(alpha <= -200){
            initThunder();
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
