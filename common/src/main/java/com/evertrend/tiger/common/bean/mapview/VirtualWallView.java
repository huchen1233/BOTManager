package com.evertrend.tiger.common.bean.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.evertrend.tiger.common.bean.event.map.AddVirtualWall;
import com.evertrend.tiger.common.bean.event.map.AddWall;
import com.evertrend.tiger.common.bean.event.map.ClearVirtualWalls;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.slamtec.slamware.geometry.Line;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class VirtualWallView extends SlamwareBaseView {
    private final static String TAG = VirtualWallView.class.getName();
    private static final int MODE_NONE = 0;
    private static final int MODE_DRAW = 1;
    private static final int MODE_ADD = 2;
    private static final int MODE_DRAW_CLEAR = 3;
    private static final int MODE_DO_CLEAR = 4;

    private static final int ERROR_VALUE = 10;
    private int drawMode = MODE_NONE;
    private Line mWall;

    private List<Line> mLines;
    private Paint mPaint;
    private int mColor;

    private float LINE_WIDTH = 1;

    WeakReference<MapView> parent;

    public VirtualWallView(Context context, WeakReference<MapView> parent, int color) {
        super(context, parent);
        this.parent = parent;
        mColor = color;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1);
    }

    public void setLines(List<Line> lines) {
        mLines = lines;
        postInvalidate();
    }

    public void drawLine(Line wall) {
        mWall = wall;
        drawMode = MODE_DRAW;
        postInvalidate();
    }
    public void addLine(Line wall) {
        mWall = wall;
        drawMode = MODE_ADD;
        postInvalidate();
    }
    public void drawClearArea(Line wall) {
        mWall = wall;
        drawMode = MODE_DRAW_CLEAR;
        postInvalidate();
    }
    public void doClearArea(Line wall) {
        mWall = wall;
        drawMode = MODE_DO_CLEAR;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(LINE_WIDTH * getScale());
        mPaint.setColor(mColor);
        if (drawMode == MODE_NONE) {
            if (mLines == null) return;
            drawWall(canvas);
        } else if (drawMode == MODE_DRAW) {
            drawWall(canvas);
            if (mWall == null) return;
            canvas.drawLine(mWall.getStartX(), mWall.getStartY(), mWall.getEndX(), mWall.getEndY(), mPaint);
        } else if (drawMode == MODE_ADD) {
            drawWall(canvas);
            if (mWall == null) return;
            canvas.drawLine(mWall.getStartX(), mWall.getStartY(), mWall.getEndX(), mWall.getEndY(), mPaint);
            EventBus.getDefault().post(new AddVirtualWall(mWall));
            drawMode = MODE_NONE;
        } else if (drawMode == MODE_DRAW_CLEAR) {
            drawWall(canvas);
            if (mWall == null) return;
            canvas.drawRect(mWall.getStartX(), mWall.getStartY(), mWall.getEndX(), mWall.getEndY(), mPaint);
        } else if (drawMode == MODE_DO_CLEAR) {
            areaClearWalls(canvas);
            mPaint.setColor(mColor);
            canvas.drawRect(mWall.getStartX(), mWall.getStartY(), mWall.getEndX(), mWall.getEndY(), mPaint);
            drawMode = MODE_NONE;
        }
    }

    private void areaClearWalls(Canvas canvas) {
        if (mLines != null) {
            List<Line> clearLines = new ArrayList<>();
            for (Line line : mLines) {
                PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
                PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
                if (start.x >= (mWall.getStartX()-ERROR_VALUE) && start.y >= (mWall.getStartY()-ERROR_VALUE) &&
                        end.x <= (mWall.getEndX()+ERROR_VALUE) && end.y <= (mWall.getEndY()+ERROR_VALUE)) {
                    mPaint.setColor(Color.BLACK);
                    canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
                    clearLines.add(line);
                } else {
                    mPaint.setColor(mColor);
                    canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
                }
            }
            if (clearLines.size() > 0) {
                EventBus.getDefault().post(new ClearVirtualWalls(clearLines));
            }
        }
    }

    private void drawWall(Canvas canvas) {
        if (mLines != null) {
            for (Line line : mLines) {
                PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
                PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
                canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
            }
        }
    }
}