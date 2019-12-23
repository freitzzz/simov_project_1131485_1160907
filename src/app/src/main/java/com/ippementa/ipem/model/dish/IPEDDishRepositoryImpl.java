package com.ippementa.ipem.model.dish;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IPEDDishRepositoryImpl implements DishRepository{

    public List<Dish> dishes(long schoolId, long canteenId, long menuId) throws IOException {

        URL url = new URL("https://heroku-iped.herokuapp.com/schools/" + schoolId + "/canteens/" + canteenId + "/menus/" + menuId + "/dishes");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            MenuDishesResponsePayload payload = new Gson().fromJson(apiResponse.payload, MenuDishesResponsePayload.class);

            List<Dish> dishes = new ArrayList<>();

            for (MenuDishesResponsePayload.Item item : payload) {

                Dish dish = new Dish();

                String typeAsString = item.type;

                Dish.DishType type;

                switch(typeAsString.toLowerCase()){
                    case "meat":
                        type = Dish.DishType.MEAT;
                        break;
                    case "fish":
                        type = Dish.DishType.FISH;
                        break;
                    case "vegetarian":
                        type = Dish.DishType.VEGETARIAN;
                        break;
                    default:
                        type = Dish.DishType.DIET;
                }

                dish.type = type;

                dish.description = item.description;

                dish.id = item.id;

                dish.menuId = menuId;

                dishes.add(dish);

            }

            return dishes;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}
