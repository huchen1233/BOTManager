package com.evertrend.tiger.common.bean.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.RobotSpot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class VirtualTraceSpotView extends SlamwareBaseView {
    private final static String TAG = VirtualTraceSpotView.class.getName();

    private List<RobotSpot> mTraces;
    private Paint mPaint;
    private Paint mCirclePaint;
    private int mColor;
    private Context mContext;
    private List<RobotSpot> mRobotSpotList;
    private float width;
    private float height;
    private boolean canTouch = false;

    private float LINE_WIDTH = 1;

    WeakReference<MapView> parent;

    public VirtualTraceSpotView(Context context, WeakReference<MapView> parent, int color) {
        super(context, parent);
        this.parent = parent;
        this.mContext = context;
        mColor = color;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1);

        mCirclePaint = mPaint;
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(10);
    }

    public void setLines(List<RobotSpot> traces) {
        mTraces = traces;

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTraces == null) {
            canTouch = false;
            return;
        }

        canTouch = true;
        mPaint.setStrokeWidth(LINE_WIDTH * getScale());
        mRobotSpotList = new ArrayList<>();
        int size = mTraces.size();
        Bitmap deleteBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.current_location);
        width = deleteBitmap.getWidth()*0.5f;
        height = deleteBitmap.getHeight()*0.5f;
        for (int i = 0; i < size; i++) {
            if (i == size-1) {
//                canvas.drawLine(mTraces.get(i-1).getX(), mTraces.get(i-1).getY(), mTraces.get(i).getX(), mTraces.get(i).getY(), mPaint);
                canvas.drawBitmap(deleteBitmap, mTraces.get(i).getX()-width, mTraces.get(i).getY()-height, mPaint);
//                RobotSpot robotSpot = mTraces.get(i);
//                robotSpot.setDeleteNum(i);
//                mRobotSpotList.add(robotSpot);
            } else {
                canvas.drawLine(mTraces.get(i).getX(), mTraces.get(i).getY(), mTraces.get(i+1).getX(), mTraces.get(i+1).getY(), mPaint);
            }
//            if (i == 0) {
//                canvas.drawCircle(mTraces.get(i).getX(), mTraces.get(i).getY(), 10, mCirclePaint);
//            } else if (i%10==0) {
//                canvas.drawLine(mTraces.get(i-1).getX(), mTraces.get(i-1).getY(), mTraces.get(i).getX(), mTraces.get(i).getY(), mPaint);
//                canvas.drawCircle(mTraces.get(i).getX(), mTraces.get(i).getY(), 10, mCirclePaint);
//            } else if (i%5==0) {
//                canvas.drawLine(mTraces.get(i-1).getX(), mTraces.get(i-1).getY(), mTraces.get(i).getX(), mTraces.get(i).getY(), mPaint);
//                canvas.drawBitmap(deleteBitmap, mTraces.get(i).getX()-width, mTraces.get(i).getY()-height, mPaint);
//                RobotSpot robotSpot = mTraces.get(i);
//                robotSpot.setDeleteNum(i);
//                mRobotSpotList.add(robotSpot);
//            } else if (i == size-1) {
//                canvas.drawLine(mTraces.get(i-1).getX(), mTraces.get(i-1).getY(), mTraces.get(i).getX(), mTraces.get(i).getY(), mPaint);
//                canvas.drawCircle(mTraces.get(i).getX(), mTraces.get(i).getY(), 10, mCirclePaint);
//            } else {
//                canvas.drawLine(mTraces.get(i-1).getX(), mTraces.get(i-1).getY(), mTraces.get(i).getX(), mTraces.get(i).getY(), mPaint);
//            }
        }
    }

    /**
     * Android基于Touch的事件分发机制为当return true时，表示会由该控件消费该事件，
     * 会把该触控事件分发出去，这就导致到MotionEvent.ACTION_DOWN 、MotionEvent.ACTION_MOVE 、
     * MotionEvent.ACTION_UP 、MotionEvent.ACTION_CANCEL(如果触发，一般触控一下只触发前三个）
     * 四个事件都调用一次onTouch，这时候OnTouch重复触发
     * return true时，可以通过when（case）语句去分别处理这四个事件，
     * return false，这时候，触控只会调用一次基于MotionEvent.ACTION_DOWN事件的onTouch方法体。
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if (canTouch) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float x = event.getX();
                float y = event.getY();
                LogUtil.i(TAG, "onTouchEvent X:"+x);
                LogUtil.i(TAG, "onTouchEvent Y:"+y);

                if (mRobotSpotList.size() > 0) {
                    for (int i = 0; i < mRobotSpotList.size(); i++) {
                        RobotSpot robotSpot = mRobotSpotList.get(i);
                        if ((x >= robotSpot.getX()-width && x <= robotSpot.getX()+width)
                                && (y >= robotSpot.getY()-height && y <= robotSpot.getY()+height)) {
                            LogUtil.i(TAG, "delete id="+robotSpot.getId());
                            LogUtil.i(TAG, "delete num="+robotSpot.getDeleteNum());
                            EventBus.getDefault().postSticky(new DeleteTraceSpotListEvent(mTraces, robotSpot));
                        }
                    }
                }
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }*/
        if (canTouch) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }
}