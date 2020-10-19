package com.evertrend.tiger.common.bean.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVtrackCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVtrackEvent;
import com.evertrend.tiger.common.bean.event.map.EditVtracksComplete;
import com.evertrend.tiger.common.bean.event.map.MoveOneVtrackEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.slamtec.slamware.geometry.Line;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class VirtualTrackLineView extends SlamwareBaseView {
    private final static String TAG = VirtualTrackLineView.class.getName();

    private List<Line> mLines;
    private Paint mPaint;
    private Paint mCirclePaint;
    private int mColor;

    private float LINE_WIDTH = 1;
    private int TOUCH_AREA = 50;

    WeakReference<MapView> parent;
    private int type;
    private Context mContext;
    private float width;
    private float height;
    private boolean canTouch = false;
    private boolean canMove = false;
    private boolean canShowMove = false;
    private int canMoveType;
    private Line canMoveLine;
    private List<PointF> pointFList;

    public VirtualTrackLineView(Context context, WeakReference<MapView> parent, int color) {
        super(context, parent);
        this.mContext = context;
        this.parent = parent;
        mColor = color;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(5);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mColor);
        mCirclePaint.setStrokeWidth(10);
    }

    public void setLines(List<Line> lines, int type) {
        mLines = lines;
        this.type = type;
        postInvalidate();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(DeleteOneVtrackCompleteEvent messageEvent) {
        LogUtil.i(mContext, TAG, "===move DeleteOneVtrackCompleteEvent===");
        if (CommonConstants.TYPE_MOVE_VIRTUAL_TRACK == messageEvent.getType()) {
            canShowMove = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(EditVtracksComplete messageEvent) {
        LogUtil.i(mContext, TAG, "===EditVtracksComplete===");
        canTouch = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLines == null) {
            canTouch = false;
            return;
        }

//        mPaint.setStrokeWidth(LINE_WIDTH * getScale());
//        canTouch = false;
        if (CommonConstants.TYPE_ADD_VIRTUAL_TRACK == type) {
            canTouch = true;
            for (Line line : mLines) {
                PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
                PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
                canvas.drawCircle(start.x, start.y, 10, mCirclePaint);
                canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
                canvas.drawCircle(end.x, end.y, 10, mCirclePaint);
            }
        } else if (CommonConstants.TYPE_DELETE_VIRTUAL_TRACK == type) {
            canTouch = true;
            Bitmap deleteBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.delete_trace_one_list);
            width = deleteBitmap.getWidth()*0.5f;
            height = deleteBitmap.getHeight()*0.5f;
            pointFList = new ArrayList<>();
            for (Line line : mLines) {
                PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
                PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
//                PointF pointF = new PointF((start.x+end.x)/2-width, (start.x+end.x)/2-width);
                PointF pointF = new PointF((start.x+end.x)/2, (start.y+end.y)/2);
                pointFList.add(pointF);
                canvas.drawBitmap(deleteBitmap, pointF.x-width, pointF.y-height, mPaint);
                canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
            }
        } else {
            for (Line line : mLines) {
                PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
                PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
                canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
            }
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
//        LogUtil.i(mContext, TAG, "track onTouchEvent canTouch : "+canTouch);
//        LogUtil.i(mContext, TAG, "track onTouchEvent type : "+type);
        if (canTouch) {
            if (CommonConstants.TYPE_DELETE_VIRTUAL_TRACK == type) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float x = event.getX();
                    float y = event.getY();
                    LogUtil.i(mContext, TAG, "onTouchEvent X:"+x);
                    LogUtil.i(mContext, TAG, "onTouchEvent Y:"+y);

                    if (pointFList != null) {
                        if (pointFList.size() > 0) {
                            for (int i = 0; i < pointFList.size(); i++) {
                                PointF pointF = pointFList.get(i);
                                if ((x >= pointF.x-width && x <= pointF.x+width)
                                        && (y >= pointF.y-height && y <= pointF.y+height)) {
                                    EventBus.getDefault().post(new DeleteOneVtrackEvent(mLines.get(i)));
                                }
                            }
                        }
                    }
                }
            } else if (CommonConstants.TYPE_ADD_VIRTUAL_TRACK == type) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    LogUtil.i(mContext, TAG, "onTouchEvent down X:"+x);
                    LogUtil.i(mContext, TAG, "onTouchEvent down Y:"+y);

                    if (mLines != null) {
                        if (mLines.size() > 0) {
                            for (int i = 0; i < mLines.size(); i++) {
                                Line line = mLines.get(i);
                                PointF start = parent.get().mapCoordinateToWidghtCoordinateF(line.getStartPoint());
                                PointF end = parent.get().mapCoordinateToWidghtCoordinateF(line.getEndPoint());
                                if ((x >= start.x-TOUCH_AREA && x <= start.x+TOUCH_AREA)
                                        && (y >= start.y-TOUCH_AREA && y <= start.y+TOUCH_AREA)) {
                                    LogUtil.i(mContext, TAG, "move line start");
                                    EventBus.getDefault().post(new MoveOneVtrackEvent(line, CommonConstants.TYPE_MOVE_VIRTUAL_TRACK, false));
                                    canMove = true;
                                    canMoveLine = line;
                                    canMoveType = CommonConstants.TYPE_MOVE_VIRTUAL_TRACK_START;
                                    break;
                                } else if ((x >= end.x-TOUCH_AREA && x <= end.x+TOUCH_AREA)
                                        && (y >= end.y-TOUCH_AREA && y <= end.y+TOUCH_AREA)) {
                                    LogUtil.i(mContext, TAG, "move line end");
                                    EventBus.getDefault().post(new MoveOneVtrackEvent(line, CommonConstants.TYPE_MOVE_VIRTUAL_TRACK, false));
                                    canMove = true;
                                    canMoveLine = line;
                                    canMoveType = CommonConstants.TYPE_MOVE_VIRTUAL_TRACK_END;
                                    break;
                                }
                            }
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE && canMove) {
                    float x = event.getX();
                    float y = event.getY();
                    LogUtil.i(mContext, TAG, "onTouchEvent move X:"+x);
                    LogUtil.i(mContext, TAG, "onTouchEvent move Y:"+y);
                    if (canShowMove) {
                        com.slamtec.slamware.geometry.PointF pointF = parent.get().widgetCoordinateToMapCoordinate(x, y);
                        if (canMoveType == CommonConstants.TYPE_MOVE_VIRTUAL_TRACK_END) {
                            canMoveLine.setEndPoint(pointF);
                        } else if (canMoveType == CommonConstants.TYPE_MOVE_VIRTUAL_TRACK_START) {
                            canMoveLine.setStartPoint(pointF);
                        }
                        EventBus.getDefault().post(new MoveOneVtrackEvent(canMoveLine, CommonConstants.TYPE_MOVE_VIRTUAL_TRACK, false));
                        for (Line line : mLines) {
                            if (line.getSegmentId() == canMoveLine.getSegmentId()) {
                                mLines.remove(line);
                                mLines.add(canMoveLine);
                                parent.get().setVtracks(mLines, CommonConstants.TYPE_ADD_VIRTUAL_TRACK);
                                break;
                            }
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP && canMove) {
                    float x = event.getX();
                    float y = event.getY();
                    LogUtil.i(mContext, TAG, "onTouchEvent up X:"+x);
                    LogUtil.i(mContext, TAG, "onTouchEvent up Y:"+y);
                    com.slamtec.slamware.geometry.PointF pointF = parent.get().widgetCoordinateToMapCoordinate(x, y);
                    if (canMoveType == CommonConstants.TYPE_MOVE_VIRTUAL_TRACK_END) {
                        canMoveLine.setEndPoint(pointF);
                        EventBus.getDefault().post(new MoveOneVtrackEvent(canMoveLine, CommonConstants.TYPE_MOVE_VIRTUAL_TRACK, true));
                    } else if (canMoveType == CommonConstants.TYPE_MOVE_VIRTUAL_TRACK_START) {
                        canMoveLine.setStartPoint(pointF);
                        EventBus.getDefault().post(new MoveOneVtrackEvent(canMoveLine, CommonConstants.TYPE_MOVE_VIRTUAL_TRACK, true));
                    }
                    canMove = false;
                    canShowMove = false;
                }
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        EventBus.getDefault().register(this);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetachedFromWindow();
    }
}