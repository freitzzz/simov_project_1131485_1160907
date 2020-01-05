package com.ippementa.ipem.model.pushnotifications;

import java.io.IOException;

public interface PushNotificationsRepository {

    void enablePushNotificationsForFavoriteDish(String registrationToken, String description, String type) throws IOException;

    void disablePushNotificationsForFavoriteDish(String registrationToken, String description, String type) throws IOException;

    void triggerNearbyCanteenPushNotification(String registrationToken, long schoolId, long canteenId) throws IOException;

}
