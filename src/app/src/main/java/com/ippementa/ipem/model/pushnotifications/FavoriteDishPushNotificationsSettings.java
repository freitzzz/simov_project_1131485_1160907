package com.ippementa.ipem.model.pushnotifications;

import com.google.gson.annotations.SerializedName;

public class FavoriteDishPushNotificationsSettings {

    @SerializedName("registration_token")
    public String registrationToken;

    public boolean isFavorite;

    public String description;

    public String type;

}
