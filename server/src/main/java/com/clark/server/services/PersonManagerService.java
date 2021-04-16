package com.clark.server.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.clark.server.R;
import com.clark.server.aidl_server.PersonManagerStub;
import com.clark.server.aidl_server.bean.PersonBean;

import java.util.ArrayList;
import java.util.List;

public class PersonManagerService extends Service {

    private static final String TAG="[aidl_remote]=>"+PersonManagerService.class.getSimpleName();
    /**
     * 创建一个数组用于管理人员  var 声明可变变量，val声明不可变变量 相当于final
     */
    private ArrayList<PersonBean> persons = new ArrayList();
    String CHANNEL_ID = "personmanager";
    int notificationId = 1;
    String CHANNEL_NAME = "violation_alive_service";
    @Override
    public void onCreate() {
        createNotificationChannel();
        //初始化通知栏，
        showNotification();
    }

    private Binder binder=new PersonManagerStub() {
        @Override
        public void addPerson(PersonBean personBean) throws RemoteException {
            if (personBean!=null){
                persons.add(personBean);
                Log.d(TAG, "addPerson: "+personBean.getName());
                showNotification();
            }
        }

        @Override
        public void deletePerson(PersonBean personBean) throws RemoteException {
            int index=-1;
            for (int i = 0; i < persons.size(); i++) {
                if (TextUtils.equals(personBean.getName(),persons.get(i).getName())){
                    index=i;
                }
            }
            if (index!=-1)
            persons.remove(index);
            Log.d(TAG, "deletePerson: "+personBean.getName());
            showNotification();
        }

        @Override
        public List<PersonBean> getPersons() throws RemoteException {
            return persons;
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    private void showNotification() {
        Notification notification = null;
        Notification.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setContentTitle("0fdg")
                .setStyle(new Notification.BigTextStyle().bigText("当前的人数是：" + persons.size() + "人员信息：" + persons.toString()))
                .setSmallIcon(R.mipmap.ic_launcher)//不设置smallicon，文字信息不生效，我服了
                .setPriority(Notification.PRIORITY_DEFAULT);
        notification = builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.flags |= Notification.FLAG_NO_CLEAR;
            startForeground(notificationId, notification);
        }
    }
    private void createNotificationChannel(){
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
            notificationChannel.setLightColor(Color.RED);//小圆点颜色
            notificationChannel.setShowBadge(true);//是否在久按桌面图标时显示此渠道的通知
            notificationChannel.setSound(null, null);
            notificationChannel.enableVibration(false);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
    }

}
