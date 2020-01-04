package com.ippementa.ipem.presenter.canteen;

import android.os.Parcel;
import android.os.Parcelable;

public class UserLocationOnMap implements Parcelable {

    public double latitude;

    public double longitude;

    protected UserLocationOnMap(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<UserLocationOnMap> CREATOR = new Creator<UserLocationOnMap>() {
        @Override
        public UserLocationOnMap createFromParcel(Parcel in) {
            return new UserLocationOnMap(in);
        }

        @Override
        public UserLocationOnMap[] newArray(int size) {
            return new UserLocationOnMap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
