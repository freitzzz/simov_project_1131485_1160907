package com.ippementa.ipem.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * An utility class that mediates external communication requests between both parties
 */
public class CommunicationMediator {

    public static boolean hasInternetConnection(Context context){

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

}
