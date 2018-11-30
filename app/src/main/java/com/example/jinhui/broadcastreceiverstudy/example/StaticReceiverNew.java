package com.example.jinhui.broadcastreceiverstudy.example;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class StaticReceiverNew extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Tag","静态注册广播接收到您正在拨打电话"+ getResultData());
    }
}
