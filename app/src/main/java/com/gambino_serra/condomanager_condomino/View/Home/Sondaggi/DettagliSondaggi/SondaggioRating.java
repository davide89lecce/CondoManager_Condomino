package com.gambino_serra.condomanager_condomino.View.Home.Sondaggi.DettagliSondaggi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_condomino.Model.Entity.Sondaggio;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.View.DrawerMenu.MainDrawer;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;


public class SondaggioRating extends AppCompatActivity {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String username = "";
    String data = "";
    String segnalazione = "";
    String usernameCondomino = "";
    String condominio = "";
    String telefonoAmministratore = "";
    String fornitore = "";
    String stato = "";
    String idCondominio = "";
    String impiantoNome = "";
    String azienda = "";
    String condomino = "";
    String uidCondomino;

    String idSondaggio;

    // Oggetti di Layout NUOVI
    TextView mOggettoSondaggio;
    TextView mDescrizioneSondaggio;
    TextView mVotazione;
    RatingBar mRating;
    ConstraintLayout btnInvia;


    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Map<String, Object> sondaggioMap;

    Bundle bundle;

    float votazione = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.compila_sondaggio_rating);

        firebaseDatabase = FirebaseDatabase.getInstance();  // PER LE TRANSACTION
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uidCondomino = firebaseUser.getUid();

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            idSondaggio = bundle.get("idSondaggio").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("idSondaggio", idSondaggio);
            editor.apply();
        } else {
            idSondaggio = sharedPrefs.getString("idSondaggio", "").toString();

            bundle = new Bundle();
            bundle.putString("idSondaggio", idSondaggio);
        }

        // Avvaloro i nuovi rierimenti al layout
        mOggettoSondaggio = (TextView) findViewById(R.id.D_OggettoSondaggio);
        mDescrizioneSondaggio = (TextView) findViewById(R.id.D_DescrizioneSondaggio);
        mVotazione = (TextView) findViewById(R.id.D_votazione);
        mRating = (RatingBar) findViewById(R.id.ratingBar);
        btnInvia = (ConstraintLayout) findViewById(R.id.btnConferma);

        Query sondaggio;
        sondaggio = FirebaseDB.getSondaggi().orderByKey().equalTo(idSondaggio);

        sondaggio.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                sondaggioMap = new HashMap<String, Object>();
                // Avvalora il primo oggetto del map con l'ID del sondaggio recuperato
                sondaggioMap.put("idSondaggio", idSondaggio);

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    sondaggioMap.put(child.getKey(), child.getValue());
                }

                final Sondaggio sondaggio = new Sondaggio(
                        sondaggioMap.get("idSondaggio").toString(),
                        sondaggioMap.get("tipologia").toString(),
                        sondaggioMap.get("oggetto").toString(),
                        sondaggioMap.get("descrizione").toString(),
                        sondaggioMap.get("data").toString(),
                        sondaggioMap.get("stato").toString()
                );

                mOggettoSondaggio.setText( sondaggio.getOggetto() );
                mDescrizioneSondaggio.setText( sondaggio.getDescrizione() );
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


    @Override
    protected void onStart() {
        super.onStart();

        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Assegna la votazione ogni volta che viene cambiato il valore sulla rating bar
                votazione = rating;

                mVotazione.setText( votazione + " / 5 ");
            }
        });



        //Firebase prova = FirebaseDB.getSondaggi().child(idSondaggio).child("scelte");
        final DatabaseReference sondaggioRef =  firebaseDatabase.getReference("Sondaggi").child(idSondaggio).child("risposte");


        btnInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                votazione = mRating.getRating();

                if ( votazione != 0 ) {

                    sondaggioRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {

                            // Legge il valore del nodo num_scelta1
                            Float somma = (Float) 0.0f ;
                            Integer num_risposte = 0 ;
                            if (mutableData.getValue() != null) {
                                //Legge il valore del nodo counter
                                somma = mutableData.child("somma").getValue(Float.class);
                                num_risposte = mutableData.child("num_risposte").getValue(Integer.class);
                            }
                            // Incrementa
                            somma = somma + votazione;
                            num_risposte = num_risposte + 1;
                            // Setta il valore aggiornato
                            mutableData.child("somma").setValue(somma);
                            mutableData.child("num_risposte").setValue(num_risposte);

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                        }
                    });


                    // INSERISCE IL CONDOMINO NELLA SEZIONE "PARTECIPATO" ED AGGIORNA IL NUMERO DI PARTECIPANTI
                    final DatabaseReference partecipato = firebaseDatabase.getReference("Sondaggi").child(idSondaggio).child("partecipanti");

                    partecipato.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Integer counter = 0;
                            //controllo necessario dato che se viene letto 0 e lo si ritiene valore nullo
                            if (mutableData.getValue() != null) {
                                //Legge il valore del nodo counter
                                counter = mutableData.child("num_partecipanti").getValue(Integer.class);
                            }
                            //Incrementa counter
                            counter = counter + 1;

                            mutableData.child(uidCondomino).setValue(true);
                            mutableData.child("num_partecipanti").setValue(counter);

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {

                            Intent intent = new Intent(getApplicationContext(), MainDrawer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Sondaggio inviato con successo", Toast.LENGTH_LONG);


                        }
                    });

                }else{
                    Toast.makeText( getApplicationContext(), "Inserire una valutazione da 1 a 5", Toast.LENGTH_LONG);
                }



            }
        });


    }
}