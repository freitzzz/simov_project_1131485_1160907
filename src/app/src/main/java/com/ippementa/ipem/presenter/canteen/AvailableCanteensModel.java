package com.ippementa.ipem.presenter.canteen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AvailableCanteensModel extends ArrayList<AvailableCanteensModel.Item> {

    public static class Item implements Parcelable {

        public long schoolId;

        public long id;

        public String name;

        public Item(){}

        protected Item(Parcel in) {
            schoolId = in.readLong();
            id = in.readLong();
            name = in.readString();
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(schoolId);
            dest.writeLong(id);
            dest.writeString(name);
        }
    }
}
