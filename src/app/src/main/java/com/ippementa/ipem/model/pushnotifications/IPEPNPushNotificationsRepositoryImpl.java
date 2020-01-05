package com.ippementa.ipem.model.pushnotifications;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;

public class IPEPNPushNotificationsRepositoryImpl implements PushNotificationsRepository{

    @Override
    public void enablePushNotificationsForFavoriteDish(String registrationToken, String description, String type) throws IOException {

        URL url = new URL("https://heroku-ipepn.herokuapp.com/dishes");

        FavoriteDishPushNotificationsSettings dish = new FavoriteDishPushNotificationsSettings();

        dish.registrationToken = registrationToken;

        dish.isFavorite = true;

        dish.description = description;

        dish.type = type;

        String json = new Gson().toJson(dish, FavoriteDishPushNotificationsSettings.class);

        Client.Response apiResponse = Client.post(url, json);

        if(apiResponse.statusCode != 200){

            throw new RequestException(apiResponse);

        }

    }

    @Override
    public void disablePushNotificationsForFavoriteDish(String registrationToken, String description, String type) throws IOException {

        URL url = new URL("https://heroku-ipepn.herokuapp.com/dishes");

        FavoriteDishPushNotificationsSettings dish = new FavoriteDishPushNotificationsSettings();

        dish.registrationToken = registrationToken;

        dish.isFavorite = false;

        dish.description = description;

        dish.type = type;

        String json = new Gson().toJson(dish, FavoriteDishPushNotificationsSettings.class);

        Client.Response apiResponse = Client.post(url, json);

        if(apiResponse.statusCode != 200){

            throw new RequestException(apiResponse);

        }

    }

}
