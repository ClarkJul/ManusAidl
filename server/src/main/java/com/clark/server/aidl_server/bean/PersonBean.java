package com.clark.server.aidl_server.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonBean implements Parcelable {
    private String name;

    public PersonBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonBean(Parcel in) {
        name = in.readString();
    }

    public static final Creator<PersonBean> CREATOR = new Creator<PersonBean>() {
        @Override
        public PersonBean createFromParcel(Parcel in) {
            return new PersonBean(in);
        }

        @Override
        public PersonBean[] newArray(int size) {
            return new PersonBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
