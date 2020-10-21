package com.evertrend.tiger.common.bean.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.evertrend.tiger.common.bean.mapview.utils.GraphicalUtil;
import com.slamtec.slamware.robot.Pose;

import java.lang.ref.WeakReference;

public class ZeroPointView extends SlamwareBaseView {
    private static final String TAG = "ZeroPointView";

    private Paint mPaint;
    private PointF zeroPointF;
    private PointF xPointF;
    private PointF yPointF;

    private float LINE_WIDTH = 1;
    private float RADIUS = 2;
    private float HEIGHT = 6;
    private float BOTTOM = 2;

    public ZeroPointView(Context context, WeakReference<MapView> parent) {
        super(context, parent);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1f);
    }

    public void drawZeroAxis() {
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(LINE_WIDTH * getScale());
        zeroPointF = mParent.get().mapCoordinateToWidghtCoordinateF(0f, 0f);
        xPointF = mParent.get().mapCoordinateToWidghtCoordinateF(2f, 0f);
        yPointF = mParent.get().mapCoordinateToWidghtCoordinateF(0f, 2f);

        mPaint.setColor(Color.RED);
        GraphicalUtil.drawArrow(canvas, mPaint, zeroPointF.x, zeroPointF.y, xPointF.x, xPointF.y, HEIGHT*getScale(),BOTTOM*getScale());
        mPaint.setColor(Color.GREEN);
        GraphicalUtil.drawArrow(canvas, mPaint, zeroPointF.x, zeroPointF.y, yPointF.x, yPointF.y, HEIGHT*getScale(),BOTTOM*getScale());
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(zeroPointF.x, zeroPointF.y, RADIUS*getScale(), mPaint);
    }

}
