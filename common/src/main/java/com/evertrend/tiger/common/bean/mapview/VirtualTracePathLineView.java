package com.evertrend.tiger.common.bean.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.slamtec.slamware.geometry.Line;

import java.lang.ref.WeakReference;
import java.util.List;


public class VirtualTracePathLineView extends SlamwareBaseView {
    private final static String TAG = VirtualTracePathLineView.class.getName();

    private List<Line> mLines;
    private Paint mPaint;
    private Paint mCirclePaint;
    private int mColor;

    private float LINE_WIDTH = 1;
    private boolean clearLine = false;

    WeakReference<MapView> parent;

    public VirtualTracePathLineView(Context context, WeakReference<MapView> parent, int color) {
        super(context, parent);
        this.parent = parent;
        mColor = color;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mColor);
        mCirclePaint.setStrokeWidth(10);
    }

    public void setLines(List<Line> lines) {
        mLines = lines;
        clearLine = false;
        postInvalidate();
    }

    public void setLines(List<Line> lines, int color) {
        mLines = lines;
        clearLine = false;
        mPaint.setColor(color);
        postInvalidate();
    }

    public void setLines(List<Line> lines, boolean isClear) {
        mLines = lines;
        clearLine = isClear;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLines == null) return;

        mPaint.setStrokeWidth(LINE_WIDTH * getScale());
        int size = mLines.size();
        for (int i = 0; i < size; i++) {
            Line line = mLines.get(i);
            PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
            PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
            if (clearLine) {
                mPaint.setColor(00000000);
                canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
            } else {
                if (i == 0) {
                    Path path3 = new Path();
                    path3.moveTo(start.x, start.y);
                    path3.lineTo(start.x-30, start.y);
                    path3.lineTo(start.x-15, start.y-30);
                    path3.close();
                    canvas.drawPath(path3, mCirclePaint);
                } else {
                    canvas.drawCircle(start.x, start.y, 10, mCirclePaint);
                }
                canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
                if (i == size -1) {
                    canvas.drawCircle(end.x, end.y, 10, mCirclePaint);
                }
            }
        }
    }

}