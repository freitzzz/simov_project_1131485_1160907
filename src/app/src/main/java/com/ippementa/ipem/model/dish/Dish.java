package com.ippementa.ipem.model.dish;

import com.ippementa.ipem.model.menu.Menu;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity
public class Dish {

    @PrimaryKey
    public long id;

    @ForeignKey(entity = Menu.class, parentColumns = "id", childColumns = "menuId")
    public long menuId;

    public String description;

    public boolean isFavorite;

    @TypeConverters(DishTypeConverter.class)
    public DishType type;

    public Dish(){}

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Dish)){
            return false;
        }else{
            return obj.hashCode() == hashCode();
        }
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Dish {" + id + "}";
    }

    public enum DishType{

        MEAT,
        FISH,
        VEGETARIAN,
        DIET

    }

    public static class DishTypeConverter {

        @TypeConverter
        public static String fromDishTypeToString(DishType dishType){

            switch (dishType){
                case MEAT:
                    return "meat";
                case FISH:
                    return "fish";
                case VEGETARIAN:
                    return "vegetarian";
                default:
                    return "diet";
            }

        }

        @TypeConverter
        public static DishType fromStringToDishType(String dishType){

            switch (dishType){
                case "meat":
                    return DishType.MEAT;
                case "fish":
                    return DishType.FISH;
                case "vegetarian":
                    return DishType.VEGETARIAN;
                default:
                    return DishType.DIET;
            }

        }

        public DishTypeConverter(){}

    }

}
