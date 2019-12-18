package com.ippementa.ipem.model.dish;

import com.ippementa.ipem.model.menu.Menu;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Dish {

    @PrimaryKey
    public long id;

    @ForeignKey(entity = Menu.class, parentColumns = "id", childColumns = "menuId")
    public long menuId;

    public String description;

    public DishType type;

    public enum DishType{

        MEAT,
        FISH,
        VEGETARIAN,
        DIET

    }

}
