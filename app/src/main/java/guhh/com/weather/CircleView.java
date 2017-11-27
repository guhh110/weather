package guhh.com.weather;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.icu.util.Measure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.path;

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
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
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
        int centerPointX = width/2;
        int centerPointY = height/2;

        float bigCircleRadius = width/2f;
        float smallCircleRadius = width/3f;

        float circlePointX = width/2f;
        float circlePointY = bigCircleRadius;
        Path path = new Path();
        path.addCircle(circlePointX,circlePointY,bigCircleRadius, Path.Direction.CW);

        Path path1 = new Path();
        path1.addCircle(circlePointX,circlePointY,smallCircleRadius, Path.Direction.CW);

        paint.setColor(Color.WHITE);
       canvas.drawPath(path,paint);

        paint.setColor(Color.parseColor("#393939"));
        paint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.SOLID));
        canvas.drawPath(path1,paint);

        paint.setStrokeWidth((float) 5.0);              //设置线宽
        canvas.drawLine(circlePointX,circlePointY, circlePointX, height, paint);

    }
}
