package com.evertrend.tiger.common.bean.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.evertrend.tiger.common.bean.event.map.AddVirtualTrack;
import com.evertrend.tiger.common.bean.event.map.AddVirtualWall;
import com.evertrend.tiger.common.bean.event.map.ClearVirtualTracks;
import com.evertrend.tiger.common.bean.event.map.ClearVirtualWalls;
import com.slamtec.slamware.geometry.Line;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class VirtualTrackView extends SlamwareBaseView {
    private final static String TAG = VirtualTrackView.class.getName();
    private static final int MODE_NONE = 0;
    private static final int MODE_DRAW = 1;
    private static final int MODE_ADD = 2;
    private static final int MODE_DRAW_CLEAR = 3;
    private static final int MODE_DO_CLEAR = 4;

    private static final int ERROR_VALUE = 10;
    private int drawMode = MODE_NONE;
    private Line mTrack;

    private List<Line> mLines;
    private Paint mPaint;
    private int mColor;

    private float LINE_WIDTH = 1;

    WeakReference<MapView> parent;

    public VirtualTrackView(Context context, WeakReference<MapView> parent, int color) {
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

    public void drawLine(Line track) {
        mTrack = track;
        drawMode = MODE_DRAW;
        postInvalidate();
    }
    public void addLine(Line track) {
        mTrack = track;
        drawMode = MODE_ADD;
        postInvalidate();
    }
    public void drawClearArea(Line track) {
        mTrack = track;
        drawMode = MODE_DRAW_CLEAR;
        postInvalidate();
    }
    public void doClearArea(Line track) {
        mTrack = track;
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
            if (mTrack == null) return;
            canvas.drawLine(mTrack.getStartX(), mTrack.getStartY(), mTrack.getEndX(), mTrack.getEndY(), mPaint);
        } else if (drawMode == MODE_ADD) {
            drawWall(canvas);
            if (mTrack == null) return;
            canvas.drawLine(mTrack.getStartX(), mTrack.getStartY(), mTrack.getEndX(), mTrack.getEndY(), mPaint);
            EventBus.getDefault().post(new AddVirtualTrack(mTrack));
            drawMode = MODE_NONE;
        } else if (drawMode == MODE_DRAW_CLEAR) {
            drawWall(canvas);
            if (mTrack == null) return;
            canvas.drawRect(mTrack.getStartX(), mTrack.getStartY(), mTrack.getEndX(), mTrack.getEndY(), mPaint);
        } else if (drawMode == MODE_DO_CLEAR) {
            areaClearWalls(canvas);
            mPaint.setColor(mColor);
            canvas.drawRect(mTrack.getStartX(), mTrack.getStartY(), mTrack.getEndX(), mTrack.getEndY(), mPaint);
            drawMode = MODE_NONE;
        }
    }

    private void areaClearWalls(Canvas canvas) {
        if (mLines != null) {
            List<Line> clearLines = new ArrayList<>();
            for (Line line : mLines) {
                PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
                PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
                if (start.x >= (mTrack.getStartX()-ERROR_VALUE) && start.y >= (mTrack.getStartY()-ERROR_VALUE) &&
                        end.x <= (mTrack.getEndX()+ERROR_VALUE) && end.y <= (mTrack.getEndY()+ERROR_VALUE)) {
                    mPaint.setColor(Color.BLACK);
                    canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
                    clearLines.add(line);
                } else {
                    mPaint.setColor(mColor);
                    canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
                }
            }
            if (clearLines.size() > 0) {
                EventBus.getDefault().post(new ClearVirtualTracks(clearLines));
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