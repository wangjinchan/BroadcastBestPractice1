package com.example.broadcastbestpractice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


public class BaseActivity extends AppCompatActivity {

    private NotificationReceiver notificationReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter_notice = new IntentFilter();
        intentFilter_notice.addAction("com.example.broadcastbestpractice.NOTICE_TEST");
        notificationReceiver = new NotificationReceiver();
        registerReceiver(notificationReceiver, intentFilter_notice);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(notificationReceiver != null){
            unregisterReceiver(notificationReceiver);
            notificationReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            //获取通知管理实例
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //8.0一张版本判断
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel channel=new NotificationChannel("important","Important",NotificationManager.IMPORTANCE_HIGH);
                assert manager != null;
                manager.createNotificationChannel(channel);
            }
            //通知点击事项
            Intent intent1=new Intent(context,NotificationActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent1,0);

            Notification notification= new NotificationCompat.Builder(context,"important")
                    .setContentTitle("收到一条通知")
                    .setContentText("你好")
                    .setSmallIcon(R.mipmap.ic_launcher)//通知图标
                    .setContentIntent(pendingIntent)//点击跳到通知详情
                    .setAutoCancel(true)//当点击通知后显示栏的通知不再显示
                    .build();
            assert manager != null;
            manager.notify(1,notification);

        }
    }

}
