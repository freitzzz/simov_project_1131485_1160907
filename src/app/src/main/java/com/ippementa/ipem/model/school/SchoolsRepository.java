package com.ippementa.ipem.model.school;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;

public class SchoolsRepository {

    public AvailableSchoolsModel availableSchools() throws IOException {

        URL url = new URL("http://10.0.2.2:8080/schools");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            AvailableSchoolsModel model = new Gson().fromJson(apiResponse.payload, AvailableSchoolsModel.class);

            return model;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}
