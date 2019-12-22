package com.ippementa.ipem.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages the settings in which the user can opt in
 */
public class Settings {

    private boolean cachedInOfflineMode;

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

    public boolean isInOfflineMode(){

        return cachedInOfflineMode;

    }

    private void reloadSettings(Context ctx){

        SharedPreferences preferences
                = ctx.getSharedPreferences("ipp-ementa-shared-preferences", Context.MODE_PRIVATE);

        boolean offlineMode = preferences.getBoolean("in-offline-mode", false);

        this.cachedInOfflineMode = offlineMode;

    }

}
