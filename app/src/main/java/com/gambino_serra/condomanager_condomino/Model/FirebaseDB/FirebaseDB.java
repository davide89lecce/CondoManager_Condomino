package com.gambino_serra.condomanager_condomino.Model.FirebaseDB;

import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by Antonio on 15/06/17.
 */

public class FirebaseDB {

    private static Firebase firebase;
    private static Context mCtx;

    private FirebaseDB(Context context) {
        mCtx = context;
    }

    public static synchronized Firebase getFirebase(){
        if(firebase == null) {
            firebase = new Firebase("https://condomanager-a5aa6.firebaseio.com/");
        }
        return firebase;
    }

}
