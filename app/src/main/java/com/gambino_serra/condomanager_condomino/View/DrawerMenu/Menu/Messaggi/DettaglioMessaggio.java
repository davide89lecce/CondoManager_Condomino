package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Messaggi;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_condomino.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class DettaglioMessaggio extends AppCompatActivity {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String username = "";
    String idMessaggio;
    String data = "";
    String tipologia = "";
    String descrizione = "";
    String foto = "";

    TextView messaggio_data;
    TextView messaggio_tipologia;
    TextView messaggio_descrizione;
    ImageView messaggio_foto;

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Map<String, Object> MessaggioMap;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_messaggio);

        firebaseAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            idMessaggio = bundle.get("id").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("id", idMessaggio);
            editor.apply();
            }
        else {
            idMessaggio = sharedPrefs.getString("id", "").toString();
            bundle = new Bundle();
            bundle.putString("id", idMessaggio);
            }

        messaggio_data = (TextView) findViewById(R.id.messaggio_data);
        messaggio_descrizione = (TextView) findViewById(R.id.messaggio_descrizione);
        messaggio_tipologia = (TextView) findViewById(R.id.messaggio_tipologia);
        messaggio_foto = (ImageView) findViewById(R.id.messaggio_foto);

        Query prova;
        prova = FirebaseDB.getMessaggiCondomino().orderByKey().equalTo(idMessaggio);

        prova.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String temp = dataSnapshot.getKey().toString();

                MessaggioMap = new HashMap<String, Object>();
                MessaggioMap.put("id", dataSnapshot.getKey());

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    MessaggioMap.put(child.getKey(), child.getValue());
                }

                MessaggioCondomino messaggio = new MessaggioCondomino(
                        MessaggioMap.get("id").toString(),
                        MessaggioMap.get("data").toString(),
                        MessaggioMap.get("tipologia").toString(),
                        MessaggioMap.get("messaggio").toString(),
                        MessaggioMap.get("uidCondomino").toString(),
                        MessaggioMap.get("uidAmministratore").toString(),
                        MessaggioMap.get("stabile").toString(),
                        MessaggioMap.get("foto").toString() );

                messaggio_tipologia.setText(messaggio.getTipologia());
                messaggio_data.setText(messaggio.getData());
                messaggio_descrizione.setText(messaggio.getMessaggio());
                //messaggio_foto.setText(); todo: caricare foto

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }

        });
    }
}