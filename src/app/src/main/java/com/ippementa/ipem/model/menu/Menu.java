package com.ippementa.ipem.model.menu;

import com.ippementa.ipem.model.canteen.Canteen;
import com.ippementa.ipem.model.dish.Dish;

import java.util.List;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Menu {

    @PrimaryKey
    public long id;

    @ForeignKey(entity = Canteen.class, parentColumns = "id", childColumns = "canteenId")
    public long canteenId;

    public MenuType type;

    public List<Dish> dishes;

    public Menu(){}

    public enum MenuType{

        LUNCH,
        DINNER

    }

}
