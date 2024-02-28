package com.example.edukannada.Application;

import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

public class EduKannadaApplication extends Application {
    private static EduKannadaApplication mInstance;
    private static boolean activityVisible;

    public static synchronized EduKannadaApplication getInstance() {
        return mInstance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(new ConnectivityReceiver(), intentFilter);
        mInstance = this;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        Log.d("onResume", "setConnectivityListener:");
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
