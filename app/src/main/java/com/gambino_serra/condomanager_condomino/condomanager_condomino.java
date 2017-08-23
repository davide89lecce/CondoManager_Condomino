package com.gambino_serra.condomanager_condomino;


import android.app.Application;

import com.firebase.client.Firebase;

public class condomanager_condomino extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
