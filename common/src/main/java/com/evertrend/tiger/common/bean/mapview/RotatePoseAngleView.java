package com.evertrend.tiger.common.bean.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.evertrend.tiger.common.bean.event.map.AddNavigationLocation;
import com.evertrend.tiger.common.bean.mapview.utils.GraphicalUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.Pose;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;


public class RotatePoseAngleView extends SlamwareBaseView {
    private final static String TAG = RotatePoseAngleView.class.getName();
    private static final int MODE_NONE = 0;
    private static final int MODE_DRAW = 1;
    private static final int MODE_ADD = 2;
    private int drawMode = MODE_NONE;
    private Line mLine;
    private PointF zeroPointF;

    private Paint mPaint;
    private int mColor;

    private float LINE_WIDTH = 1;

    WeakReference<MapView> parent;

    public RotatePoseAngleView(Context context, WeakReference<MapView> parent, int color) {
        super(context, parent);
        this.parent = parent;
        mColor = color;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);//空心
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1);//圆环宽度
    }

    public void setLine(Line line) {
        mLine = line;
        drawMode = MODE_DRAW;
        postInvalidate();
    }

    public void addLocation(Line line) {
        mLine = line;
        drawMode = MODE_ADD;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(LINE_WIDTH * getScale());
        zeroPointF = mParent.get().mapCoordinateToWidghtCoordinateF(0f, 0f);
        if (mLine == null) return;
        if (drawMode == MODE_DRAW) {
            float x1 = mLine.getStartX()+mLine.getEndX()-zeroPointF.x;
            float y1 = mLine.getStartY()+mLine.getEndY()-zeroPointF.y;
            GraphicalUtil.drawArrow(canvas, mPaint, mLine.getStartX(), mLine.getStartY(), x1, y1, 30,10);
//            drawArrow(canvas, zeroPointF.x, zeroPointF.y, mLine.getEndX(), mLine.getEndY(), 30,10);
        } else if (drawMode == MODE_ADD) {
            float radians = getRadians();
//            LogUtil.d(TAG, "radians: "+radians);
            float x1 = mLine.getStartX()+mLine.getEndX()-zeroPointF.x;
            float y1 = mLine.getStartY()+mLine.getEndY()-zeroPointF.y;
            GraphicalUtil.drawArrow(canvas, mPaint, mLine.getStartX(), mLine.getStartY(), x1, y1, 30,10);
//            drawArrow(canvas, zeroPointF.x, zeroPointF.y, mLine.getEndX(), mLine.getEndY(), 30,10);
            EventBus.getDefault().post(new AddNavigationLocation(mLine.getStartX(), mLine.getStartY(), radians));
            drawMode = MODE_NONE;
        }
    }

    private float getRadians() {
        com.slamtec.slamware.geometry.PointF endF = mParent.get().widgetCoordinateToMapCoordinate(mLine.getEndX(), mLine.getEndY());
        // 两点在X轴的距离
        float lenX = (float) (endF.getX() - 0);
        // 两点在Y轴距离
        float lenY = (float) (endF.getY() - 0);
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
        float radians = (float) (Math.acos(lenX / lenXY) * (endF.getY() < 0 ? -1 : 1));
        return radians;
    }

    /**
     *
     * @param vertexPointX -- 角度对应顶点X坐标值
     * @param vertexPointY -- 角度对应顶点Y坐标值
     * @param point0X
     * @param point0Y
     * @param point1X
     * @param point1Y
     * @return
     */
    private float getDegree(float vertexPointX, float vertexPointY, float point0X, float point0Y, float point1X, float point1Y) {
        //向量的点乘
        float vector = (point0X - vertexPointX) * (point1X - vertexPointX) + (point0Y - vertexPointY) * (point1Y - vertexPointY);
        //向量的模乘
        double sqrt = Math.sqrt(
                (Math.abs((point0X - vertexPointX) * (point0X - vertexPointX)) + Math.abs((point0Y - vertexPointY) * (point0Y - vertexPointY)))
                        * (Math.abs((point1X - vertexPointX) * (point1X - vertexPointX)) + Math.abs((point1Y - vertexPointY) * (point1Y - vertexPointY)))
        );
        //反余弦计算弧度
        double radian = Math.acos(vector / sqrt);
        //弧度转角度制
        return (float) radian;
    }
}