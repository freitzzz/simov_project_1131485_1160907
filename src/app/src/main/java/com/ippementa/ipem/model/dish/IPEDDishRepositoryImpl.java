package com.ippementa.ipem.model.dish;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;

public class IPEDDishRepositoryImpl implements DishRepository{

    public MenuDishesResponsePayload dishes(long schoolId, long canteenId, long menuId) throws IOException {

        URL url = new URL("http://localhost:8080/schools/" + schoolId + "/canteens/" + canteenId + "/menus/" + menuId + "/dishes");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            MenuDishesResponsePayload payload = new Gson().fromJson(apiResponse.payload, MenuDishesResponsePayload.class);

            return payload;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}
