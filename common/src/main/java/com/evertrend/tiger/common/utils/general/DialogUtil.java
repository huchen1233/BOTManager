package com.evertrend.tiger.common.utils.general;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.bean.event.ProgressStopEvent;

import org.greenrobot.eventbus.EventBus;

public class DialogUtil {
    public static final String TAG = DialogUtil.class.getCanonicalName();
    private static ProgressDialog progressDialog;
    private static AlertDialog choiceDialog;

    public static void showChoiceDialog(Context context, String message, final int type) {
        choiceDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new DialogChoiceEvent(type));
                        choiceDialog.dismiss();
                    }
                })
                .create();
        choiceDialog.show();
    }

    public static void showChoiceDialog(Context context, int messageId, final int type) {
        choiceDialog = new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new DialogChoiceEvent(type));
                        choiceDialog.dismiss();
                    }
                })
                .create();
        choiceDialog.show();
    }

    public static void showMessageDialog(Context context, int messageId, final int type) {
        choiceDialog = new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new DialogChoiceEvent(type));
                        choiceDialog.dismiss();
                    }
                })
                .create();
        choiceDialog.show();
    }

    public static void showProgressDialog(final Context context, String message, boolean cancelable, boolean canceledOnTouchOutside) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtil.i(context, TAG, "cancel");
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

    public static void showToast(Context context, String message, int showTime) {
        Toast.makeText(context, message, showTime).show();
    }

    public static void showToast(Context context, int messageId, int showTime) {
        Toast.makeText(context, messageId, showTime).show();
    }

    public static void showSuccessToast(Context context) {
        Toast.makeText(context, R.string.yl_common_success, Toast.LENGTH_SHORT).show();
    }
}
