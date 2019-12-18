package com.ippementa.ipem.model.menu;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;

public class IPEDMenusRepositoryImpl implements MenusRepository{

    public AvailableCanteenMenusResponsePayload availableMenus(long schoolId, long canteenId) throws IOException {

        URL url = new URL("http://localhost:8080/schools/" + schoolId + "/canteens/" + canteenId + "/menus");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            AvailableCanteenMenusResponsePayload payload = new Gson().fromJson(apiResponse.payload, AvailableCanteenMenusResponsePayload.class);

            return payload;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}