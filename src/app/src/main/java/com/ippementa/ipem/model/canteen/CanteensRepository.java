package com.ippementa.ipem.model.canteen;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;

public class CanteensRepository {

    public AvailableCanteensModel availableCanteens(long schoolId) throws IOException {

        URL url = new URL("http://10.0.2.2:8080/schools/" + schoolId + "/canteens");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            AvailableCanteensModel model = new Gson().fromJson(apiResponse.payload, AvailableCanteensModel.class);

            return model;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}