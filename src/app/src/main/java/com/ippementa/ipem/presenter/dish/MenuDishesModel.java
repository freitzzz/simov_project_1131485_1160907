package com.ippementa.ipem.presenter.dish;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MenuDishesModel extends ArrayList<MenuDishesModel.Item> {

    public static class Item implements Parcelable {

        public long id;

        public MenuDishesModel.Item.Type type;

        public String typeAsString;

        public String description;

        public Item(){}

        protected Item(Parcel in) {
            id = in.readLong();
            typeAsString = in.readString();
            description = in.readString();
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
            dest.writeString(typeAsString);
            dest.writeString(description);
            dest.writeString(type.name());
        }

        public enum Type {
            MEAT,
            FISH,
            VEGETARIAN,
            DIET
        }
    }
}
