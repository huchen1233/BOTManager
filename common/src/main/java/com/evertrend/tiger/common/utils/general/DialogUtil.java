package com.evertrend.tiger.common.utils.general;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.evertrend.tiger.common.bean.event.ProgressStopEvent;

import org.greenrobot.eventbus.EventBus;

public class DialogUtil {
    public static final String TAG = DialogUtil.class.getCanonicalName();
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String message, boolean cancelable, boolean canceledOnTouchOutside) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtil.i(TAG, "cancel");
                EventBus.getDefault().post(new ProgressStopEvent());
            }
        });
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
