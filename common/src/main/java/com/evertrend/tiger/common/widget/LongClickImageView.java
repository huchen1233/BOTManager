package com.evertrend.tiger.common.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import static java.lang.Thread.sleep;

public class LongClickImageView extends androidx.appcompat.widget.AppCompatImageView {
    private LongClickRepeatListener mRepeatListener;

    private long mIntervalTime;

    private boolean mNeedHandle;

    private MyHandler mHandler;

    public LongClickImageView(Context context) {
        super(context);
        init();
    }

    public LongClickImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LongClickImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mNeedHandle = false;
        mHandler = new MyHandler(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread(new LongClickThread()).start();
                return true;
            }
        });
    }
    private class LongClickThread implements Runnable {

        @Override
        public void run() {
            while (LongClickImageView.this.isPressed()) {
                mHandler.sendEmptyMessage(1);
                mNeedHandle = true;

                try {
                    sleep(mIntervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<LongClickImageView> ref;

        MyHandler(LongClickImageView imageView) {
            ref = new WeakReference<>(imageView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LongClickImageView imageView = ref.get();
            if (imageView != null && imageView.mRepeatListener != null) {
                imageView.mRepeatListener.repeatAction(imageView);
            }
        }
    }

    public void setLongClickRepeatListener(LongClickRepeatListener listener, long intervalTime) {
        this.mRepeatListener = listener;
        this.mIntervalTime = intervalTime;
    }

    public void setLongClickRepeatListener(LongClickRepeatListener listener) {
        setLongClickRepeatListener(listener, 100);
    }

    public interface LongClickRepeatListener {
        void repeatAction(View view);
    }
}
