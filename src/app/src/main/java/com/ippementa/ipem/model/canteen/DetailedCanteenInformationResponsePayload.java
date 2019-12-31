package com.ippementa.ipem.model.canteen;

public class DetailedCanteenInformationResponsePayload {

    public long id;

    public String name;

    public Location location;

    public DetailedCanteenInformationResponsePayload(){}

    public class Location {

        public double latitude;

        public double longitude;

        public Location(){}

    }
}
