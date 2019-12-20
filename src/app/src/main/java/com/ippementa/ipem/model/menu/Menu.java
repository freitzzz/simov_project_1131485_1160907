package com.ippementa.ipem.model.menu;

import com.ippementa.ipem.model.canteen.Canteen;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity
public class Menu {

    @PrimaryKey
    public long id;

    @ForeignKey(entity = Canteen.class, parentColumns = "id", childColumns = "canteenId")
    public long canteenId;

    @TypeConverters(MenuTypeConverter.class)
    public MenuType type;

    //public List<Dish> dishes;

    public Menu(){}

    public enum MenuType{

        LUNCH,
        DINNER
    }

    public static class MenuTypeConverter {

        @TypeConverter
        public static String fromMenuTypeToString(MenuType menuType){

            switch (menuType){
                case LUNCH:
                    return "lunch";
                default:
                    return "dinner";
            }

        }

        @TypeConverter
        public static MenuType fromStringToMenuType(String menuType){

            switch (menuType){
                case "lunch":
                    return MenuType.LUNCH;
                default:
                    return MenuType.DINNER;
            }

        }

        public MenuTypeConverter(){}

    }

}
