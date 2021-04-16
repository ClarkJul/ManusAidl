package com.clark.client.aidl;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.clark.client.aidl.bean.PersonBean;
import java.util.List;


public class PersonManagerProxy implements PersonManager {
    /**
     * 远程binder对象
     */
    IBinder remote;
    private static final String DESCRIPTOR = "com.clark.server.aidl_server.PersonManager";

    /**
     * 构造方法传入ibinder
     *
     * @param remote
     */
    public PersonManagerProxy(IBinder remote) {
        this.remote = remote;
    }

    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }


    @Override
    public void addPerson(PersonBean personBean) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (personBean != null) {
                data.writeInt(1);
                personBean.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            remote.transact(PersonManagerStub.TRANSAVTION_addperson, data, replay, 0);
            replay.readException();
        } finally {
            replay.recycle();
            data.recycle();
        }
    }

    @Override
    public void deletePerson(PersonBean personBean) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (personBean != null) {
                data.writeInt(1);
                personBean.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            remote.transact(PersonManagerStub.TRANSAVTION_deleteperson, data, replay, 0);
            replay.readException();
        } finally {
            replay.recycle();
            data.recycle();
        }
    }

    @Override
    public List<PersonBean> getPersons() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        List<PersonBean> result;
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            remote.transact(PersonManagerStub.TRANSAVTION_getpersons, data, replay, 0);
            replay.readException();
            result = replay.createTypedArrayList(PersonBean.CREATOR);
        } finally {
            replay.recycle();
            data.recycle();
        }
        return result;
    }

    @Override
    public IBinder asBinder() {
        return remote;
    }
}
