package com.ippementa.ipem.model.school;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IPEDSchoolsRepositoryImpl implements SchoolsRepository{

    public List<School> availableSchools() throws IOException {

        URL url = new URL("http://demo7718589.mockable.io/schools");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            AvailableSchoolsResponsePayload model = new Gson().fromJson(apiResponse.payload, AvailableSchoolsResponsePayload.class);

            List<School> schools = new ArrayList<>();

            for (AvailableSchoolsResponsePayload.Item item : model) {

                School school = new School();

                school.acronym = item.acronym;

                school.name = item.name;

                school.id = item.id;

                schools.add(school);

            }

            return schools;
        }else{

            throw new RequestException(apiResponse);

        }
    }

}
