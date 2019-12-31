package com.ippementa.ipem.presenter.canteen;

import android.os.Parcel;
import android.os.Parcelable;

public class CanteenWithMapLocationModel implements Parcelable {

    public long id;

    public String name;

    public double latitude;

    public double longitude;

    public CanteenWithMapLocationModel(){}

    protected CanteenWithMapLocationModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CanteenWithMapLocationModel> CREATOR = new Creator<CanteenWithMapLocationModel>() {
        @Override
        public CanteenWithMapLocationModel createFromParcel(Parcel in) {
            return new CanteenWithMapLocationModel(in);
        }

        @Override
        public CanteenWithMapLocationModel[] newArray(int size) {
            return new CanteenWithMapLocationModel[size];
        }
    };
}
