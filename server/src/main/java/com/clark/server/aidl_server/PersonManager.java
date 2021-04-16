package com.clark.server.aidl_server;

import android.os.IInterface;
import android.os.RemoteException;

import com.clark.server.aidl_server.bean.PersonBean;

import java.util.List;


public interface PersonManager extends IInterface {
    final String DESCRIPTOR = "com.clark.server.aidl_server.PersonManager";
    /**
     * 添加人数
     *
     * @throws RemoteException
     */
    void addPerson(PersonBean personBean) throws RemoteException;

    /**
     * 删除人数
     *
     * @throws RemoteException
     */
    void deletePerson(PersonBean personBean) throws RemoteException;

    /**
     * 获取人数
     *
     * @throws RemoteException
     */
    List<PersonBean> getPersons() throws RemoteException;
}
