package com.evertrend.tiger.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evertrend.tiger.common.activity.LogConfigActivity;

public class LogSettingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent bluetoothIntent = new Intent(context, LogConfigActivity.class);
        bluetoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(bluetoothIntent);
    }
}
