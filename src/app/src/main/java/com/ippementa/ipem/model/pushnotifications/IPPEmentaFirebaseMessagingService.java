package com.ippementa.ipem.model.pushnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ippementa.ipem.MainActivity;
import com.ippementa.ipem.R;
import com.ippementa.ipem.presenter.canteen.CanteenWithMapLocationModel;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.canteen.RouteToCanteenActivity;
import com.ippementa.ipem.view.dish.MenuDishesActivity;

import java.util.Map;

import androidx.core.app.NotificationCompat;

public class IPPEmentaFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d("fcm", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Provider.instance(getApplicationContext()).settings().changeFcmRegistrationToken(getApplicationContext(), token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("fcm", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            requestNotification(remoteMessage);

            /*if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("fcm", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void requestNotification(RemoteMessage remoteMessage) {

        Class activityToOpen;

        String clickAction = remoteMessage.getNotification().getClickAction();

        Log.d("fcm", "onMessageReceived: " + remoteMessage.getData().get("message"));

        Log.d("fcm", "Message data payload: " + remoteMessage.getData());

        Map<String, String> data = remoteMessage.getData();

        String messageType = data.get("type");

        if(clickAction.equalsIgnoreCase("FCM_OPEN_ROUTE_TO_CANTEEN_ACTIVITY")) {

            activityToOpen = RouteToCanteenActivity.class;

        }else if(clickAction.equalsIgnoreCase("FCM_OPEN_MENU_DISHES_ACTIVITY")) {

            activityToOpen = MenuDishesActivity.class;

        }else {

            activityToOpen = MainActivity.class;

        }


        Intent intent = new Intent(this, activityToOpen);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        if(messageType != null && messageType.equalsIgnoreCase("favorite-dish")) {

            long schoolId = Long.parseLong(data.get("schoolId"));

            long canteenId = Long.parseLong(data.get("canteenId"));

            long menuId = Long.parseLong(data.get("menuId"));

            AvailableCanteenMenusModel.Item menu = new AvailableCanteenMenusModel.Item();

            menu.schoolId = schoolId;

            menu.canteenId = canteenId;

            menu.id = menuId;

            intent.putExtra("menu", menu);

        }else if(messageType != null && messageType.equalsIgnoreCase("nearby-canteen")) {

            double latitude = Double.parseDouble(data.get("latitude"));

            double longitude = Double.parseDouble(data.get("longitude"));

            String name = data.get("name");

            CanteenWithMapLocationModel canteen = new CanteenWithMapLocationModel();

            canteen.name = name;

            canteen.latitude = latitude;

            canteen.longitude = longitude;

            intent.putExtra("canteen-location", canteen);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Default";

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);

            manager.createNotificationChannel(channel);
        }

        manager.notify(0, builder.build());

    }

}
