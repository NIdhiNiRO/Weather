package com.virtusa.weather.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.greenrobot.event.EventBus;
/**
 * Created by nidhiparekh on 10/11/17.
 */
public class InternetConnectionBroadCast extends BroadcastReceiver {
    public InternetConnectionBroadCast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        EventBus.getDefault().post(new GetConnection(isConnected));
    }

    public class GetConnection{
        public boolean isConnected;
        public GetConnection(boolean isConnected){
            this.isConnected=isConnected;
        }
    }
}
