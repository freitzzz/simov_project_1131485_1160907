package com.ippementa.ipem.model.canteen;

import com.google.gson.Gson;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IPEDCanteensRepositoryImpl implements CanteensRepository {

    public List<Canteen> canteens(long schoolId) throws IOException {

        URL url = new URL("https://heroku-iped.herokuapp.com/schools/" + schoolId + "/canteens");

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            AvailableCanteensResponsePayload payload = new Gson().fromJson(apiResponse.payload, AvailableCanteensResponsePayload.class);


            List<Canteen> canteens = new ArrayList<>();

            for (AvailableCanteensResponsePayload.Item item : payload) {

                Canteen canteen = new Canteen();

                canteen.name = item.name;

                canteen.id = item.id;

                canteen.schoolId = schoolId;

                canteens.add(canteen);

            }

            return canteens;

        }else{

            throw new RequestException(apiResponse);

        }
    }

    @Override
    public Canteen canteen(long schoolId, long canteenId) throws IOException {

        URL url = new URL("https://heroku-iped.herokuapp.com/schools/" + schoolId + "/canteens/" + canteenId);

        Client.Response apiResponse = Client.get(url);

        if(apiResponse.statusCode == 200){

            DetailedCanteenInformationResponsePayload payload = new Gson().fromJson(apiResponse.payload, DetailedCanteenInformationResponsePayload.class);

            Canteen canteen = new Canteen();

            canteen.id = canteenId;

            canteen.schoolId = schoolId;

            canteen.name = payload.name;

            Canteen.Location location = new Canteen().new Location();

            location.latitude = payload.location.latitude;

            location.longitude = payload.location.longitude;

            canteen.location = location;

            return canteen;

        }else{

            throw new RequestException(apiResponse);

        }

    }

}
