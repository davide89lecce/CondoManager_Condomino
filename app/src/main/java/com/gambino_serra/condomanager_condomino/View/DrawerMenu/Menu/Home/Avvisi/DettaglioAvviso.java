package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Avvisi;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.gambino_serra.condomanager_condomino.Model.Entity.Avviso;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DettaglioAvviso extends AppCompatActivity {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String username = "";

    String idAvviso;

    // Oggetti di Layout NUOVI
    TextView mOggetto;
    TextView mDescrizione;


    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Map<String, Object> avvisoMap;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_avviso);

        firebaseAuth = FirebaseAuth.getInstance();

        //TODO: serve???
        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            idAvviso = bundle.get("idAvviso").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("idAvviso", idAvviso);
            editor.apply();

        } else {
            //TODO: perchè
            idAvviso = sharedPrefs.getString("idSegnalazione", "").toString();

            bundle = new Bundle();
            bundle.putString("idAvviso", idAvviso);

        }

        // Avvaloro i nuovi rierimenti al layout
        mOggetto = (TextView) findViewById(R.id.Ogg_Avviso);
        mDescrizione = (TextView) findViewById(R.id.Descr_Avviso);




        Query avviso;
        avviso = FirebaseDB.getAvvisi().orderByKey().equalTo(idAvviso);

        // chiama una query per farsi restituire una """Tabella""" con tutti i figli aventi lo stesso
        // uidFornitore, ovvero solo uno, ma in questo modo possiamo inserire un addChild event listener
        // che considererà come Key gli uid (solo uno) e come Value l'intero sottoalbero

        avviso.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                avvisoMap = new HashMap<String, Object>();

                // Avvalora il primo oggetto del map con l'ID dell'avviso recuperato
                avvisoMap.put("idAvviso", dataSnapshot.getKey());

                //completa il map prima di passare ad avvalorare l'oggetto e fare il set sulle textview
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    avvisoMap.put(child.getKey(), child.getValue());
                }

                try {

                    Avviso avviso = new Avviso(
                            avvisoMap.get("idAvviso").toString(),
                            avvisoMap.get("amministratore").toString(),
                            avvisoMap.get("stabile").toString(),
                            avvisoMap.get("oggetto").toString(),
                            avvisoMap.get("descrizione").toString(),
                            avvisoMap.get("scadenza").toString()
                    );

                    mOggetto.setText(avviso.getOggetto());
                    mDescrizione.setText(avviso.getDescrizione());

                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Null Pointer Exception su uno dei campi", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
