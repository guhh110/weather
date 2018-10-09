package com.guhh.weatherviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class WeatherRelativeLayout extends CloudRelativeLayout {
    private RainMoveThread rainMoveThread = new RainMoveThread();
    private boolean isFirstTime = true;
    private boolean moving = true;
    private List<RainDropItem> rainItems = new ArrayList<>();
    private List<ThunderItem> thunderItems = new ArrayList<>();
    private int rainCount = 50;
    private int thunderCount = 5;
    public WeatherRelativeLayout(Context context) {
        super(context);
    }

    public WeatherRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CloudRelativeLayout);
        this.rainCount = typedArray.getInt(R.styleable.CloudRelativeLayout_rainCount,50);
        this.thunderCount = typedArray.getInt(R.styleable.CloudRelativeLayout_thunderCount,5);
        typedArray.recycle();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(isFirstTime){
            for(int i = 0;i<rainCount;i++){
                RainDropItem rainItem = new RainDropItem(getWidth(),getHeight(),getContext(),Color.parseColor("#FFFFFF"));
                rainItems.add(rainItem);
            }

            for(int i=0;i<thunderCount;i++){
                ThunderItem thunderItem = new ThunderItem(getWidth(),getHeight(),getContext(),Color.parseColor("#FFFF33"));
                thunderItems.add(thunderItem);
            }


            rainMoveThread.start();
            isFirstTime = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] pts = new float[rainItems.size() * 4];
        for(int i=0,j=0;i<rainItems.size();i++,j+=4){
            RainDropItem rainItem = rainItems.get(i);
//            pts[j] = rainItem.startX;
//            pts[j+1] = rainItem.startY;
//            pts[j+2] = rainItem.stopX;
//            pts[j+3] = rainItem.stopY;
            canvas.drawLine(rainItem.startX,rainItem.startY,rainItem.stopX,rainItem.stopY,rainItem.paint);
            Log.i("sssddd",rainItem.startX+"-"+rainItem.startY+"-"+rainItem.stopX+"-"+rainItem.stopY);
        }
        for (int i=0;i<thunderCount;i++){
            ThunderItem thunderItem = thunderItems.get(i);
            canvas.drawPath(thunderItem.path,thunderItem.paint);
        }
//        canvas.drawLines(pts,paint);
    }
    private class RainMoveThread extends Thread{
        @Override
        public void run() {
            while (moving){
                for(int i=0;i<rainItems.size();i++){
                    RainDropItem rainItem = rainItems.get(i);
                    rainItem.move();
                    Log.i("sssddd",rainItem.startX+"-"+rainItem.startY+"-"+rainItem.stopX+"-"+rainItem.stopY);
                }

                for(int i=0;i<thunderCount;i++){
                    ThunderItem thunderItem = thunderItems.get(i);
                    thunderItem.thunderFade();
                }

                postInvalidate();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
