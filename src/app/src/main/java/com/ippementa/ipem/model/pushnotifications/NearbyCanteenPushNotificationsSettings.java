package com.ippementa.ipem.model.pushnotifications;

import com.google.gson.annotations.SerializedName;

public class NearbyCanteenPushNotificationsSettings {

    @SerializedName("registration_token")
    public String registrationToken;

    public long schoolId;

    public long canteenId;

}
