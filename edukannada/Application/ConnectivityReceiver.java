package com.example.edukannada.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
        Log.d("onResume", "constructor:");
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.d("onResume", "setConnectivityListenerOutside:");
        if (connectivityReceiverListener != null) {
            Log.d("onResume", "connectivityReceiverListener:");
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

//    public static boolean isConnected() {
//        ConnectivityManager
//                cm = (ConnectivityManager) EduKannadaApplication.getInstance().getApplicationContext()
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//
//
//        return activeNetwork != null
//                && activeNetwork.isConnectedOrConnecting();
//
//    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
