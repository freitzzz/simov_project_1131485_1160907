package com.ippementa.ipem.model.menu;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IPEDMenusRepositoryImpl implements MenusRepository{

    public List<Menu> menus(long schoolId, long canteenId) throws IOException {

        URL url = new URL("http://localhost:8080/schools/" + schoolId + "/canteens/" + canteenId + "/menus");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            AvailableCanteenMenusResponsePayload payload = new Gson().fromJson(apiResponse.payload, AvailableCanteenMenusResponsePayload.class);

            List<Menu> menus = new ArrayList<>();

            for (AvailableCanteenMenusResponsePayload.Item item : payload) {

                Menu menu = new Menu();

                String typeAsString = item.type;

                Menu.MenuType type;

                switch(typeAsString.toLowerCase()){
                    case "lunch":
                        type = Menu.MenuType.LUNCH;
                        break;
                    default:
                        type = Menu.MenuType.DINNER;
                }

                menu.type = type;

                menu.id = item.id;

                menu.canteenId = canteenId;

                menus.add(menu);

            }

            return menus;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}
