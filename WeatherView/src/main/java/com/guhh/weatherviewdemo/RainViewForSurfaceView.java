package com.guhh.weatherviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class RainViewForSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    private RainMoveThread rainMoveThread = new RainMoveThread();
    private boolean isFirstTime = true;
    private boolean moving = true;
    private List<RainItem> rainItems = new ArrayList<>();
    private int rainCount = 200;

    private Paint paint;
    public RainViewForSurfaceView(Context context) {
        super(context);
    }

    public RainViewForSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.parseColor("#ff0000"));
        paint.setAntiAlias(true);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

    }

    public RainViewForSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(isFirstTime){
            for(int i = 0;i<rainCount;i++){
                RainItem rainItem = new RainItem(getWidth(),getHeight());
                rainItems.add(rainItem);
            }
            rainMoveThread.start();
            isFirstTime = false;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class RainMoveThread extends Thread{
        @Override
        public void run() {
            while (moving){
                //改变雨点位置
                for(int i=0;i<rainItems.size();i++){
                    RainItem rainItem = rainItems.get(i);
                    rainItem.move();

                    Log.i("sssddd",rainItem.startX+"-"+rainItem.startY+"-"+rainItem.stopX+"-"+rainItem.stopY);
                }
                //绘制
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.parseColor("#66ccff"));
                float[] pts = new float[rainItems.size() * 4];
                for(int i=0,j=0;i<rainItems.size();i++,j+=4){
                    RainItem rainItem = rainItems.get(i);
                    pts[j] = rainItem.startX;
                    pts[j+1] = rainItem.startY;
                    pts[j+2] = rainItem.stopX;
                    pts[j+3] = rainItem.stopY;
//            canvas.drawLine(rainItem.startX,rainItem.startY,rainItem.stopX,rainItem.stopY,paint);
                    Log.e("sssddd",rainItem.startX+"-"+rainItem.startY+"-"+rainItem.stopX+"-"+rainItem.stopY);
                }
                canvas.drawLines(pts,paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
                SystemClock.sleep(10);
            }
        }
    }
}
