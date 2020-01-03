package com.ippementa.ipem.presenter.menu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AvailableCanteenMenusModel extends ArrayList<AvailableCanteenMenusModel.Item> {

    public static class Item implements Parcelable {

        public long id;

        public long schoolId;

        public long canteenId;

        public Type type;

        public String typeAsString;

        public Item(){}

        protected Item(Parcel in) {
            id = in.readLong();
            schoolId = in.readLong();
            canteenId = in.readLong();
            typeAsString = in.readString();
            type = Type.valueOf(in.readString());
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
            dest.writeLong(id);
            dest.writeLong(schoolId);
            dest.writeLong(canteenId);
            dest.writeString(typeAsString);
            dest.writeString(type.name());
        }

        public enum Type {
            LUNCH,
            DINNER
        }
    }
}
