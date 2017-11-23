package guhh.com.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.icu.util.Measure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by guhh on 2017/11/23.
 */

public class CircleView extends View {
    private int width;
    private int height;
    private Paint paint;
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.addCircle(width/2,height/2,30, Path.Direction.CW);

        Path path1 = new Path();
        path1.addCircle(width/2,height/2,10, Path.Direction.CW);

        paint.setColor(Color.BLUE);
       canvas.drawPath(path,paint);

        paint.setColor(Color.BLACK);
        canvas.drawPath(path1,paint);

        paint.setColor(Color.BLACK);                    //设置画笔颜色
        paint.setStrokeWidth((float) 3.0);              //设置线宽
        canvas.drawLine(width/2, height/2, width/2, height+10, paint);

    }
}
