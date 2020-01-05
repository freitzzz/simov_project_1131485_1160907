package com.ippementa.ipem.model.canteen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.ippementa.ipem.model.pushnotifications.PushNotificationsRepository;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.http.RequestException;

import java.io.IOException;
import java.util.List;

public class UserNearbyCanteensGeofencingBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e("geof", errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            System.out.println(geofenceTransition);

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            if(triggeringGeofences != null) {

                for (Geofence geofence : triggeringGeofences) {

                    System.out.println(geofence.getRequestId());

                    // Send notification and log the transition details.
                    sendNotification(context, geofence.getRequestId());

                }

            }

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            Log.i("geof", geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e("geof", "geofence transition invalid type " + geofenceTransition);
        }


    }

    private void sendNotification(Context ctx, String geofenceRequestId) {

        final String[] geofenceSplit = geofenceRequestId.split("-");

        final long schoolId = Long.parseLong(geofenceSplit[2]);

        final long canteenId = Long.parseLong(geofenceSplit[1]);

        final String registrationToken = Provider.instance(ctx).settings().fcmRegistrationToken();

        final PushNotificationsRepository repository = Provider.instance(ctx).pushNotificationsRepository();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    repository.triggerNearbyCanteenPushNotification(registrationToken, schoolId, canteenId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RequestException f) {
                    f.printStackTrace();
                    System.out.println(f.response.statusCode);
                }

                return null;
            }
        }.execute();

    }

    private String getGeofenceTransitionDetails(UserNearbyCanteensGeofencingBroadcastReceiver userNearbyCanteensGeofencingBroadcastReceiver, int geofenceTransition, List<Geofence> triggeringGeofences) {

        return "";

    }
}
