package com.example.jinhui.broadcastreceiverstudy;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jinhui.broadcastreceiverstudy.example.DynamicReceiverNew;
import com.example.jinhui.broadcastreceiverstudy.reveiver.dynamic.DynamicReceiver;

/**
 * https://blog.csdn.net/panhouye/article/details/53588930
 * Android中BroadcastReceiver的两种注册方式（静态和动态）
 *
 * BroadcastReceiver也就是“广播接收者”的意思，
 * 顾名思义，它就是用来接收来自系统和应用中的广播。
 * 在Android系统中，广播体现在方方面面，
 *
 * 例如当开机完成后系统会产生一条广播，接收到这条广播就能实现开机启动服务的功能；
 * 当网络状态改变时系统会产生一条广播，接收到这条广播就能及时地做出提示和保存数据等操作；
 * 当电池电量改变时，系统会产生一条广播，接收到这条广播就能在电量低时告知用户及时保存进度等等。
 *
 * Android中的广播机制设计的非常出色，很多事情原本需要开发者亲自操作的，
 * 现在只需等待广播告知自己就可以了，大大减少了开发的工作量和开发周期。
 *
 * 而作为应用开发者，就需要数练掌握Android系统提供的一个开发利器，
 * 那就是BroadcastReceiver。
 *
 *
 * 在我们详细分析创建BroadcastReceiver的两种注册方式前，我们先罗列本次分析的大纲：
 *
 * （1）对静态和动态两种注册方式进行概念阐述以及演示实现步骤
 *
 * （2）简述两种BroadcastReceiver的类型（为后续注册方式的对比做准备）
 *
 * （3）在默认广播类型下设置优先级和无优先级情况下两种注册方式的比较
 *
 * （4）在有序广播类型下两种注册方式的比较
 *
 * （5）通过接受打电话的广播，在程序（Activity）运行时和终止运行时，对两种注册方式的比较
 *
 * （6）总结两种方式的特点
 *
 *
 * 第二步：为方便后续分析，这里插入BroadcastReceiver的两种常用类型
 * （1）Normalbroadcasts：默认广播
 * 发送一个默认广播使用Context.sendBroadcast(）方法，普通广播对于多个接收者来说是完全异步的，
 * 通常每个接收者都无需等待即可以接收到广播，接收者相互之间不会有影响。
 * 对于这种广播，接收者无法终止广播，即无法阻止其他接收者的接收动作。
 *
 * (2）orderedbroadcasts：有序广播
 *  发送一个有序广播使用Context.sendorderedBroadcast(）方法，有序广播比较特殊，
 *  它每次只发送到优先级较高的接收者那里，然后由优先级高的接受者再传播到优先级低的接收者那里，
 *  优先级高的接收者有能力终止这个广播。
 *
 * 发送有序广播：sendorderedBroadCast()
 *
 * 在注册广播中的<intent-filter>中使用android:priority属性。
 * 这个属性的范围在-1000到1000，数值越大，优先级越高。
 * 在广播接收器中使用setResultExtras方法将一个Bundle对象设置为结果集对象，传递到下一个接收者那里，
 * 这样优先级低的接收者可以用getResultExtras获取到最新的经过处理的信息集合。使用sendorderedBroadcast方法发送有序广播时，
 * 需要一个权限参数，如果为null则表示不要求接收者声明指定的权限，如果不为null则表示接收者若要接收此广播，需声明指定权限。
 * 这样做是从安全角度考虑的，例如系统的短信就是有序广播的形式，一个应用可能是具有拦截垃圾短信的功能，
 * 当短信到来时它可以先接受到短信广播，必要时终止广播传递，这样的软件就必须声明接收短信
 *
 *
 * 第三步：在默认广播下两种注册方式的比较
 * (1）两种注册方式均不设置优先级
 * 这里将动态与静态两种注册的广播触发集中在一个按钮上，
 * 显示效果如下（未设置优先级的情况下，先动态后静态）：
 *
 * （2）将动态优先级设置为最低-1000，静态优先级设置为最高1000
 * 显示效果如下（动态仍先于静态被接收到）：
 *
 * 第四步：在有序广播下两种注册方式比较
 * 静态广播1（优先级为200），静态广播2（优先级为300），
 * 静态广播3（优先级为400），静态广播优先级为（-100），
 * 动态广播优先级为0。显示效果如下：
 *
 * 第五步：接受打电话的广播，比较程序运行中与结束运行时，两种注册方式的比较
 * 本次比较采用比对Log的方式对两种注册方式进行比较，在MainActivity.java中会插入Activity全部生命周期用于检测Log分析。
 *
 * (1）在未退出Activity时，拨打电话，Log如下：
 *
 * // 开启app创建activity
 * I/MainActivity: Activity-onCreate
 * I/MainActivity: Activity-onStart
 * I/MainActivity: Activity-onResume
 * // 按住home键返回主页，准备拨打电话
 * I/MainActivity: Activity-onPause
 * I/MainActivity: Activity-onStop
 * // 拨打电话
 * I/Tag: 动态注册广播接收到您正在拨打电话10010
 * I/Tag: 静态注册广播接收到您正在拨打电话10010
 *
 * （2）在退出Activity时，拨打电话，Log如下（即便不解除注册，动态仍无法接受到广播）：
 * 开启app创建activity
 *  I/MainActivity: Activity-onCreate
 *  I/MainActivity: Activity-onStart
 *  I/MainActivity: Activity-onResume
 * // 按住back键销毁activity，准备拨打电话
 * I/MainActivity: Activity-onPause
 * I/MainActivity: Activity-onStop
 * I/MainActivity: Activity-onDestroy
 * I/Tag: 静态注册广播接收到您正在拨打电话10010
 *
 * 第六步：总结两种注册方式特点
 *
 * 广播接收器注册一共有两种形式：静态注册和动态注册．
 *
 * 两者及其接收广播的区别：
 *
 * （1）动态注册广播不是常驻型广播，也就是说广播跟随Activity的生命周期。注意在Activity结束前，移除广播接收器。
 *
 * 静态注册是常驻型，也就是说当应用程序关闭后，如果有信息广播来，程序也会被系统调用自动运行。
 *
 * （2）当广播为有序广播时：优先级高的先接收（不分静态和动态）。同优先级的广播接收器，动态优先于静态
 *
 * （3）同优先级的同类广播接收器，静态：先扫描的优先于后扫描的，动态：先注册的优先于后注册的。
 *
 * （4）当广播为默认广播时：无视优先级，动态广播接收器优先于静态广播接收器。同优先级的同类广播接收器，静态：先扫描的优先于后扫描的，动态：先注册的优先于后册的。
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button bt_dynamicRegister, bt_staticRegister,
            bt_all_register, bt_order_register,
            bt_call_register;

    DynamicReceiver dynamicReceiver;
    DynamicReceiverNew dynamicReceiverNew; //声明动态注册广播接收


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_dynamicRegister = findViewById(R.id.bt_dynamic_register);
        bt_staticRegister = findViewById(R.id.bt_static_register);
        bt_all_register = findViewById(R.id.bt_all_register);
        bt_order_register = findViewById(R.id.bt_order_register);
        bt_call_register = findViewById(R.id.bt_call_register);

        // 实例化IntentFilter对象
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("dynamic");
        // 将动态与静态两种注册的广播触发集中在一个按钮上
        intentFilter.addAction("custom_action");
        intentFilter.setPriority(-1000); // 设置动态优先级

        dynamicReceiver = new DynamicReceiver();
        // 注册广播接收
        registerReceiver(dynamicReceiver, intentFilter);


        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        dynamicReceiverNew = new DynamicReceiverNew();
        registerReceiver(dynamicReceiverNew, filter);
        Log.i(TAG,"Activity-onCreate");



        // 动态注册
        bt_dynamicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("dynamic");
                intent.putExtra("name", "张三");
                sendBroadcast(intent);
            }
        });


        // 静态注册
        bt_staticRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("custom_action");
                intent.putExtra("name", "李四");
                sendBroadcast(intent);
            }
        });

        // 这里将动态与静态两种注册的广播触发集中在一个按钮上，
        // 显示效果如下（未设置优先级的情况下，先动态后静态）
        bt_all_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("custom_action");
                intent.putExtra("name", "李四");
                sendBroadcast(intent);
            }
        });

        // 在有序广播下两种注册方式比较
        bt_order_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("custom_action");
                intent.putExtra("name", "李四");
                sendOrderedBroadcast(intent, null);
            }
        });

        // 接受打电话的广播，比较程序运行中与结束运行时，两种注册方式的比较
        //本次比较采用比对Log的方式对两种注册方式进行比较，在MainActivity.java中会插入Activity全部生命周期用于检测Log分析。
        bt_call_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "看demo例子", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setAction("custom_action");
//                intent.putExtra("name", "李四");
//                sendOrderedBroadcast(intent, null);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"Activity-onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"Activity-onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"Activity-onPause");
    }

    //    /**
//     * 动态注册需在Acticity生命周期onPause通过
//     * unregisterReceiver()方法移除广播接收器，
//     * 优化内存空间，避免内存溢出
//     *
//     */
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(dynamicReceiver);
//    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"Activity-onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Activity-onDestroy");
        unregisterReceiver(dynamicReceiver);
        unregisterReceiver(dynamicReceiverNew);
    }
}
