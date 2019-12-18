package com.ippementa.ipem.model.canteen;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;

public class IPEDCanteensRepositoryImpl implements CanteensRepository {

    public AvailableCanteensResponsePayload availableCanteens(long schoolId) throws IOException {

        URL url = new URL("http://localhost:8080/schools/" + schoolId + "/canteens");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            AvailableCanteensResponsePayload payload = new Gson().fromJson(apiResponse.payload, AvailableCanteensResponsePayload.class);

            return payload;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}
