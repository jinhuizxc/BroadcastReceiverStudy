package com.example.jinhui.broadcastreceiverstudy.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2018/11/30.
 * <p>
 * 动态注册
 *
 * 通过继承 BroadcastReceiver建立动态广播接收器
 */
public class DynamicReceiverNew extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Tag","动态注册广播接收到您正在拨打电话"+ getResultData());
    }
}
