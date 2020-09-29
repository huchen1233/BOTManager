package com.evertrend.tiger.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.utils.general.LogUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ActionControllerView extends View {
    private static final String TAG = "ActionControllerView";

    private int mWidth;
    private int mHeight;

    private RectF bigRectF;
    private int bigRadius;
    private RectF smallRectF;
    private int smallRadius;
    private int padding = 20;
    private int sweepAngel = 80;
    private int offsetAngel;

    @TouchArea
    private int mTouchArea = TouchArea.INVALID;

    private Paint mPaint;
    private Paint mDirectionPaint;
    private Region topRegion, bottomRegion, leftRegion, rightRegion, centerRegion, globalRegion;
    private Path topPath, bottomPath, leftPath, rightPath, centerPath, selectedPath;
    private Path rightActionPath, bottomActionPath, leftActionPath, topActionPath;

    Matrix mMapMatrix;

    private int unselectedColor = 0xff000000;
    private int selectedColor = 0xffff0000;
    private int directionColor = 0xffffffff;

    private boolean isSelected = false;

    public ActionControllerView(Context context) {
        super(context);
    }

    public ActionControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActionControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TouchArea.LEFT, TouchArea.TOP, TouchArea.RIGHT, TouchArea.BOTTOM,
            TouchArea.CENTER, TouchArea.INVALID})
    public @interface TouchArea {
        int LEFT = 1;
        int TOP = 2;
        int RIGHT = 3;
        int BOTTOM = 4;
        int CENTER = 5;
        int INVALID = 0;
    }

    private class LongClickThread implements Runnable {
        private int touchArea;

        public void setTouchArea(int touchArea) {
            this.touchArea = touchArea;
        }

        @Override
        public void run() {
            Message message = new Message();
            message.what = touchArea;
            mHandler.sendMessage(message);
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<ActionControllerView> ref;

        MyHandler(ActionControllerView view) {
            ref = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            ActionControllerView view = ref.get();
            if (view != null && view.mRepeatListener != null) {
                view.mRepeatListener.repeatAction(view, msg.what);
            }
        }
    }

    public void setLongClickRepeatListener(ActionControllerView.LongClickRepeatListener listener, long intervalTime) {
        this.mRepeatListener = listener;
        this.mIntervalTime = intervalTime;
    }

    public void setLongClickRepeatListener(ActionControllerView.LongClickRepeatListener listener) {
        setLongClickRepeatListener(listener, 100);
    }

     public interface LongClickRepeatListener {
        void repeatAction(View view, int what);
    }

    private void postAreaForLongTouch(int touchArea) {
        longClickThread.setTouchArea(touchArea);
        stopPostActionTimer();
        scheduledThreadPostAction = new ScheduledThreadPoolExecutor(5);
        scheduledThreadPostAction.scheduleAtFixedRate(longClickThread, 0, 100, TimeUnit.MILLISECONDS);
//        postDelayed(longClickThread, 200);
    }

    private void stopPostActionTimer() {
        if (scheduledThreadPostAction != null) {
            scheduledThreadPostAction.shutdownNow();
            scheduledThreadPostAction = null;
        }
    }

    private long mIntervalTime;
    private ActionControllerView.MyHandler mHandler = new MyHandler(this);
    private LongClickThread longClickThread = new LongClickThread();
    private LongClickRepeatListener mRepeatListener;
    private ScheduledThreadPoolExecutor scheduledThreadPostAction;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (longClickThread != null) {
            float[] pts = new float[2];
            pts[0] = event.getX();
            pts[1] = event.getY();
//            LogUtil.d(TAG, "原始触摸位置：" + Arrays.toString(pts) + " mMapMatrix: " + mMapMatrix);
            mMapMatrix.mapPoints(pts);

            int x = (int) pts[0];
            int y = (int) pts[1];
//            LogUtil.d(TAG, "转换后的触摸位置：" + Arrays.toString(pts) + " mMapMatrix: " + mMapMatrix);
            int touchArea = TouchArea.INVALID;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (leftRegion.contains(x, y)) {
                    touchArea = TouchArea.LEFT;
                } else if (topRegion.contains(x, y)) {
                    touchArea = TouchArea.TOP;
                } else if (rightRegion.contains(x, y)) {
                    touchArea = TouchArea.RIGHT;
                } else if (bottomRegion.contains(x, y)) {
                    touchArea = TouchArea.BOTTOM;
                } else if (centerRegion.contains(x, y)) {
                    touchArea = TouchArea.CENTER;
                } else {
                    touchArea = TouchArea.INVALID;
                }
                postAreaForLongTouch(touchArea);
                isSelected = true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (leftRegion.contains(x, y)) {
                    touchArea = TouchArea.LEFT;
                } else if (topRegion.contains(x, y)) {
                    touchArea = TouchArea.TOP;
                } else if (rightRegion.contains(x, y)) {
                    touchArea = TouchArea.RIGHT;
                } else if (bottomRegion.contains(x, y)) {
                    touchArea = TouchArea.BOTTOM;
                }  else if (centerRegion.contains(x, y)) {
                    touchArea = TouchArea.CENTER;
                } else {
                    touchArea = TouchArea.INVALID;
                    stopPostActionTimer();
                }
                isSelected = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                touchArea = TouchArea.INVALID;
                isSelected = false;
                stopPostActionTimer();
            }
            mTouchArea = touchArea;
            invalidate();
        }

        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float[] pts = new float[2];
//        pts[0] = event.getX();
//        pts[1] = event.getY();
//        LogUtil.d(TAG, "原始触摸位置：" + Arrays.toString(pts) + " mMapMatrix: " + mMapMatrix);
//        mMapMatrix.mapPoints(pts);
//
//        int x = (int) pts[0];
//        int y = (int) pts[1];
//        LogUtil.d(TAG, "转换后的触摸位置：" + Arrays.toString(pts) + " mMapMatrix: " + mMapMatrix);
//        int touchArea = TouchArea.INVALID;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (leftRegion.contains(x, y)) {
//                    touchArea = TouchArea.LEFT;
//                }
//                if (topRegion.contains(x, y)) {
//                    touchArea = TouchArea.TOP;
//                }
//                if (rightRegion.contains(x, y)) {
//                    touchArea = TouchArea.RIGHT;
//                }
//                if (bottomRegion.contains(x, y)) {
//                    touchArea = TouchArea.BOTTOM;
//                }
//                if (centerRegion.contains(x, y)) {
//                    touchArea = TouchArea.CENTER;
//                }
//                if (touchArea == TouchArea.INVALID) {
//                    mTouchArea = touchArea;
//                    LogUtil.d(TAG, "点击outside");
//                } else {
//                    if (mTouchArea == touchArea) {
//                        //取消选中
//                        isSelected = false;
//                        mTouchArea = TouchArea.INVALID;
//                    } else {
//                        //选中
//                        isSelected = true;
//                        mTouchArea = touchArea;
//                    }
//                    LogUtil.d(TAG, "按钮状态 mTouchArea " + mTouchArea + " isSelected: " + isSelected);
//                    if (mListener != null) {
//                        mListener.onMenuClicked(mTouchArea, isSelected);
//                    }
//                    invalidate();
//                }
//                break;
//        }
//
//        return true;
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(unselectedColor);
        mDirectionPaint = new Paint();
        mDirectionPaint.setAntiAlias(true);
        mDirectionPaint.setStyle(Paint.Style.FILL);
        mDirectionPaint.setStrokeWidth(10);
        mDirectionPaint.setTextSize(50);
        mDirectionPaint.setTextAlign(Paint.Align.CENTER);
        mDirectionPaint.setColor(directionColor);

        offsetAngel = (360 - sweepAngel * 4) / 4;
        bigRectF = new RectF();
        smallRectF = new RectF();

        topRegion = new Region();
        bottomRegion = new Region();
        leftRegion = new Region();
        rightRegion = new Region();
        centerRegion = new Region();
        globalRegion = new Region();
        topPath = new Path();
        bottomPath = new Path();
        leftPath = new Path();
        rightPath = new Path();
        centerPath = new Path();
        mMapMatrix = new Matrix();
        rightActionPath = new Path();
        bottomActionPath = new Path();
        leftActionPath = new Path();
        topActionPath = new Path();
        //大圆
        bigRadius = (Math.min(mWidth, mHeight) - 60) / 2;
        bigRectF.set(-bigRadius, -bigRadius, bigRadius, bigRadius);
        //小圆
        smallRadius = (bigRadius - padding - 20) / 2;
        smallRectF.set(-smallRadius - padding, -smallRadius - padding,
                smallRadius + padding, smallRadius + padding);

        mMapMatrix.reset();
        globalRegion.set(-mWidth / 2, -mHeight / 2, mWidth / 2, mHeight / 2);

        centerPath.addCircle(0, 0, smallRadius, Path.Direction.CW);
        centerRegion.setPath(centerPath, globalRegion);

        float startAngel = -sweepAngel / 2f;
        rightPath.addArc(bigRectF, startAngel, sweepAngel + 4);
        startAngel += sweepAngel;
        rightPath.arcTo(smallRectF, startAngel, -sweepAngel);
        rightPath.close();
        rightRegion.setPath(rightPath, globalRegion);

        startAngel += offsetAngel;
        bottomPath.addArc(bigRectF, startAngel, sweepAngel + 4);
        startAngel += sweepAngel;
        bottomPath.arcTo(smallRectF, startAngel, -sweepAngel);
        bottomPath.close();
        bottomRegion.setPath(bottomPath, globalRegion);

        startAngel += offsetAngel;
        leftPath.addArc(bigRectF, startAngel, sweepAngel + 4);
        startAngel += sweepAngel;
        leftPath.arcTo(smallRectF, startAngel, -sweepAngel);
        leftPath.close();
        leftRegion.setPath(leftPath, globalRegion);

        startAngel += offsetAngel;
        topPath.addArc(bigRectF, startAngel, sweepAngel + 4);
        startAngel += sweepAngel;
        topPath.arcTo(smallRectF, startAngel, -sweepAngel);
        topPath.close();
        topRegion.setPath(topPath, globalRegion);

        float startF = bigRadius - 20;
        rightActionPath.moveTo(startF, 0);
        rightActionPath.lineTo(startF - 40, 0 - 30);
        rightActionPath.lineTo(startF - 40, 0 + 30);
        rightActionPath.close();

        startF = -bigRadius + 20;
        leftActionPath.moveTo(startF, 0);
        leftActionPath.lineTo(startF + 40, 0 - 30);
        leftActionPath.lineTo(startF + 40, 0 + 30);
        leftActionPath.close();

        startF = bigRadius - 20;
        topActionPath.moveTo(0, startF);
        topActionPath.lineTo(0 - 30, startF - 40);
        topActionPath.lineTo(0 + 30, startF - 40);
        topActionPath.close();

        startF = -bigRadius + 20;
        topActionPath.moveTo(0, startF);
        topActionPath.lineTo(0 - 30, startF + 40);
        topActionPath.lineTo(0 + 30, startF + 40);
        topActionPath.close();

//        LogUtil.d(TAG, "globalRegion: " + globalRegion);
//        LogUtil.d(TAG, "globalRegion: " + globalRegion);
//        LogUtil.d(TAG, "leftRegion: " + leftRegion);
//        LogUtil.d(TAG, "topRegion: " + topRegion);
//        LogUtil.d(TAG, "rightRegion: " + rightRegion);
//        LogUtil.d(TAG, "bottomRegion: " + bottomRegion);
//        LogUtil.d(TAG, "centerRegion: " + centerRegion);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        // 获取测量矩阵(逆矩阵)
        if (mMapMatrix.isIdentity()) {
            canvas.getMatrix().invert(mMapMatrix);
        }

        mPaint.setColor(unselectedColor);
        canvas.drawPath(centerPath, mPaint);
        //计算baseline
        Paint.FontMetrics fontMetrics = mDirectionPaint.getFontMetrics();
        float distance=(fontMetrics.descent - fontMetrics.ascent)/2 - fontMetrics.descent;
        float baseline = smallRectF.centerY()+distance;
        canvas.drawText("stop", smallRectF.centerX(), baseline, mDirectionPaint);
        canvas.drawPath(rightPath, mPaint);
        canvas.drawPath(bottomPath, mPaint);
        canvas.drawPath(leftPath, mPaint);
        canvas.drawPath(topPath, mPaint);
        canvas.drawPath(rightActionPath, mDirectionPaint);
        canvas.drawPath(bottomActionPath, mDirectionPaint);
        canvas.drawPath(leftActionPath, mDirectionPaint);
        canvas.drawPath(topActionPath, mDirectionPaint);

        if (!isSelected) return;
        mPaint.setColor(selectedColor);
        switch (mTouchArea) {
            case TouchArea.LEFT:
                canvas.drawPath(leftPath, mPaint);
                break;
            case TouchArea.TOP:
                canvas.drawPath(topPath, mPaint);
                break;
            case TouchArea.RIGHT:
                canvas.drawPath(rightPath, mPaint);
                break;
            case TouchArea.BOTTOM:
                canvas.drawPath(bottomPath, mPaint);
                break;
            case TouchArea.CENTER:
                canvas.drawPath(centerPath, mPaint);
                break;
        }
//        LogUtil.e(TAG, " touchArea: " + mTouchArea);

        //Android还提供了一个RegionIterator来对Region中的所有矩阵进行迭代，
        // 可以使用该类，获得某个Region的所有矩阵
        //通过遍历region中的矩阵，并绘制出来，来绘制region
//        mPaint.setColor(Color.RED);
//        RegionIterator iterator = new RegionIterator(topRegion);
//        Rect r = new Rect();
//        while (iterator.next(r)) {
//            canvas.drawRect(r, mPaint);
//        }
//
//        mPaint.setColor(Color.BLUE);
//        RegionIterator iterator1 = new RegionIterator(leftRegion);
//        Rect r1 = new Rect();
//        while (iterator1.next(r1)) {
//            canvas.drawRect(r1, mPaint);
//        }
//
//        mPaint.setColor(Color.BLACK);
//        RegionIterator iterator2 = new RegionIterator(rightRegion);
//        Rect r2 = new Rect();
//        while (iterator2.next(r2)) {
//            canvas.drawRect(r2, mPaint);
//        }
//
//        mPaint.setColor(Color.YELLOW);
//        RegionIterator iterator3 = new RegionIterator(bottomRegion);
//        Rect r3 = new Rect();
//        while (iterator3.next(r3)) {
//            canvas.drawRect(r3, mPaint);
//        }
//
//        mPaint.setColor(Color.GREEN);
//        RegionIterator iterator4 = new RegionIterator(centerRegion);
//        Rect r4 = new Rect();
//        while (iterator4.next(r4)) {
//            canvas.drawRect(r4, mPaint);
//        }
    }

//    private MenuListener mListener;
//
//    public void setListener(MenuListener listener) {
//        mListener = listener;
//    }
//
//    // 点击事件监听器
//    public interface MenuListener {
//        void onMenuClicked(int type, boolean isSelected);
//    }
}
