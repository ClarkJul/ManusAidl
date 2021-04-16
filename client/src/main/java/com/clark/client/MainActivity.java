package com.clark.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.clark.client.aidl.PersonManager;
import com.clark.client.aidl.PersonManagerStub;
import com.clark.client.aidl.bean.PersonBean;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="[aidl_remote]=>"+MainActivity.class.getSimpleName();
    private boolean isBound=false;
    private PersonManager mService=null;
    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected:remote ");
            mService= PersonManagerStub.asInterface(service);
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected:remote");
            mService=null;
            isBound=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound){
            unbindService(mConnection);
        }
    }



    private void addListener() {
        findViewById(R.id.btn_get_persons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result=mService.getPersons().toString();
                    ((TextView)findViewById(R.id.tv_display)).setText(result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }) ;

        findViewById(R.id.btn_add_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PersonBean personBean=new PersonBean("刘德华");
                    mService.addPerson(personBean);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }) ;

        findViewById(R.id.btn_delete_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PersonBean personBean=new PersonBean("刘德华");
                    mService.deletePerson(personBean);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }) ;
        findViewById(R.id.btn_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setPackage("com.clark.server");
                intent.setAction("com.clark.aidl.remote.service");
                //intent.setComponent(new ComponentName("com.clark.server","com.clark.aidl.remote.service"));

                boolean result=bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
                Log.d(TAG, "onClick: " + result);
            }
        }) ;
    }
}