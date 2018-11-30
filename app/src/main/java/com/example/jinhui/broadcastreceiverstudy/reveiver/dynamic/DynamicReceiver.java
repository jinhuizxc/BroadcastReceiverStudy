package com.example.jinhui.broadcastreceiverstudy.reveiver.dynamic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
public class DynamicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast toast = Toast.makeText(context, "动态广播: "
                        + intent.getStringExtra("name"),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();  //方便录屏，将土司设置在屏幕顶端
    }
}
