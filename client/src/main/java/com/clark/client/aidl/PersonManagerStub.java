package com.clark.client.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.clark.client.aidl.bean.PersonBean;
import java.util.List;


public abstract class PersonManagerStub extends Binder implements PersonManager {
    //private static final String DESCRIPTOR = "com.clark.server.aidl_server.PersonManager";

    public PersonManagerStub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static PersonManager asInterface(IBinder binder) {
        if (binder == null)
            return null;
        IInterface iin = binder.queryLocalInterface(DESCRIPTOR);//通过DESCRIPTOR查询本地binder，如果存在则说明调用方和service在同一进程间，直接本地调用
        if (iin != null && iin instanceof PersonManager)
            return (PersonManager) iin;
        return new PersonManagerProxy(binder);//本地没有，返回一个远程代理对象
    }
    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;

            case TRANSAVTION_getpersons:
                data.enforceInterface(DESCRIPTOR);
                List<PersonBean> result = this.getPersons();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;

            case TRANSAVTION_addperson:
                data.enforceInterface(DESCRIPTOR);
                PersonBean arg0 = null;
                if (data.readInt() != 0) {
                    arg0 = PersonBean.CREATOR.createFromParcel(data);
                }
                this.addPerson(arg0);
                reply.writeNoException();
                return true;
            case TRANSAVTION_deleteperson:
                data.enforceInterface(DESCRIPTOR);
                PersonBean personBean = null;
                if (data.readInt() != 0) {
                    personBean = PersonBean.CREATOR.createFromParcel(data);
                }
                this.deletePerson(personBean);
                reply.writeNoException();
                return true;

        }
        return super.onTransact(code, data, reply, flags);
    }

    public static final int TRANSAVTION_getpersons = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSAVTION_addperson = IBinder.FIRST_CALL_TRANSACTION + 1;
    public static final int TRANSAVTION_deleteperson = IBinder.FIRST_CALL_TRANSACTION + 2;
}

