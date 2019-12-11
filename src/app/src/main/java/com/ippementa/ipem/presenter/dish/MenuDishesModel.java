package com.ippementa.ipem.presenter.dish;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MenuDishesModel extends ArrayList<MenuDishesModel.Item> {

    public static class Item implements Parcelable {

        public long id;

        public String type;

        public String description;

        public Item(){}

        protected Item(Parcel in) {
            id = in.readLong();
            type = in.readString();
            description = in.readString();
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
            dest.writeString(type);
            dest.writeString(description);
        }
    }
}