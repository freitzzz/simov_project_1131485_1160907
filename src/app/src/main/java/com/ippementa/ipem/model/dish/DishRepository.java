package com.ippementa.ipem.model.dish;

import com.google.gson.Gson;
import com.ippementa.ipem.model.menu.AvailableCanteenMenusResponsePayload;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;

public class DishRepository {

    public MenuDishesResponsePayload dishes(long schoolId, long canteenId, long menuId) throws IOException {

        URL url = new URL("http://demo7718589.mockable.io/schools/" + schoolId + "/canteens/" + canteenId + "/menus/" + menuId + "/dishes");

        Client.Response apiResponse = Client.get(url);

        System.out.println(url.toString());

        System.out.println(apiResponse.statusCode);

        if(apiResponse.statusCode == 200){

            MenuDishesResponsePayload payload = new Gson().fromJson(apiResponse.payload, MenuDishesResponsePayload.class);

            return payload;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}
