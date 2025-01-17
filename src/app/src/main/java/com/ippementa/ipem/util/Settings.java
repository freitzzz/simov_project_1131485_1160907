package com.ippementa.ipem.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages the settings in which the user can opt in
 */
public class Settings {

    private boolean cachedInOfflineMode;

    private boolean cachedInDarkMode;

    private String cachedFcmRegistrationToken;

    private boolean cachedAllowsFavoriteDishPushNotifications;

    private boolean cachedAllowsNearbyCanteensPushNotifications;

    protected Settings(Context ctx){

        reloadSettings(ctx);

    }

    public boolean activateOfflineMode(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("in-offline-mode", true).commit();

        if(!writtenWithSuccess){
            return false;
        }else{
            reloadSettings(ctx);
            return true;
        }
    }

    public boolean activateOnlineMode(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("in-offline-mode", false).commit();

        if(!writtenWithSuccess){
            return false;
        }else{
            reloadSettings(ctx);
            return true;
        }
    }

    public boolean activateLightMode(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("in-dark-mode", false).commit();

        if(writtenWithSuccess){
            this.cachedInOfflineMode = false;
            return true;
        }else{
            return false;
        }

    }

    public boolean activateDarkMode(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("in-dark-mode", true).commit();

        if(writtenWithSuccess){
            this.cachedInDarkMode = true;
            return true;
        }else{
            return false;
        }

    }

    public boolean allowReceiveOfFavoriteDishPushNotifications(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("allows-favorite-dish-push-notifications", true).commit();

        if(writtenWithSuccess){
            this.cachedAllowsFavoriteDishPushNotifications = true;
        }

        return writtenWithSuccess;

    }

    public boolean disallowReceiveOfFavoriteDishPushNotifications(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("allows-favorite-dish-push-notifications", false).commit();

        if(writtenWithSuccess){
            this.cachedAllowsFavoriteDishPushNotifications = false;
        }

        return writtenWithSuccess;

    }

    public boolean allowReceiveOfNearbyCanteensPushNotifications(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("allows-nearby-canteens-push-notifications", true).commit();

        if(writtenWithSuccess){
            this.cachedAllowsNearbyCanteensPushNotifications = true;
        }

        return writtenWithSuccess;

    }

    public boolean disallowReceiveOfNearbyCanteensPushNotifications(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putBoolean("allows-nearby-canteens-push-notifications", false).commit();

        if(writtenWithSuccess){
            this.cachedAllowsNearbyCanteensPushNotifications = false;
        }

        return writtenWithSuccess;

    }

    public boolean isInOfflineMode(){

        return cachedInOfflineMode;

    }

    public boolean isInDarkMode(){

        return cachedInDarkMode;

    }

    public boolean allowsReceiveOfFavoriteDishPushNotifications(){

        return cachedAllowsFavoriteDishPushNotifications;

    }

    public boolean allowsReceiveOfNearbyCanteensPushNotifications(){

        return cachedAllowsFavoriteDishPushNotifications;

    }

    public boolean changeFcmRegistrationToken(Context ctx, String newRegistrationToken) {

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean writtenWithSuccess = preferences.edit().putString("fcm-registration-token ", newRegistrationToken).commit();

        if(writtenWithSuccess){
            this.cachedFcmRegistrationToken = newRegistrationToken;
        }

        return writtenWithSuccess;

    }

    public String fcmRegistrationToken() {

        return this.cachedFcmRegistrationToken;

    }

    private void reloadSettings(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean offlineMode = preferences.getBoolean("in-offline-mode", false);

        boolean darkMode = preferences.getBoolean("in-dark-mode", false);

        boolean allowsFavoriteDishPushNotifications = preferences.getBoolean("allows-favorite-dish-push-notifications", false);

        boolean allowsNearbyCanteensPushNotifications = preferences.getBoolean("allows-favorite-dish-push-notifications", false);

        String fcmRegistrationToken = preferences.getString("fcm-registration-token", null);

        this.cachedInOfflineMode = offlineMode;

        this.cachedInDarkMode = darkMode;

        this.cachedAllowsFavoriteDishPushNotifications = allowsFavoriteDishPushNotifications;

        this.cachedAllowsNearbyCanteensPushNotifications = allowsNearbyCanteensPushNotifications;

        this.cachedFcmRegistrationToken = fcmRegistrationToken;

    }

}
