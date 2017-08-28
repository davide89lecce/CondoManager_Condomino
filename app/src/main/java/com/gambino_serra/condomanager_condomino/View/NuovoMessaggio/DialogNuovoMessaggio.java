package com.gambino_serra.condomanager_condomino.View.NuovoMessaggio;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.gambino_serra.condomanager_condomino.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class DialogNuovoMessaggio extends DialogFragment {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String uidCondomino;
    private String stabile;
    private String uidAmministratore;

    EditText descrizioneMessaggioE;
    String descrizioneMessaggio;
    String username;

    public DialogNuovoMessaggio() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        firebaseAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        TextView title = new TextView(getActivity());
        title.setText(R.string.title_nuovo_messaggio);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(30);
        title.setBackgroundResource(R.color.primarySegnalazione);
        title.setTextColor(Color.WHITE);
        builder.setCustomTitle(title);

        builder.setView(inflater.inflate(R.layout.dialog_nuova_segnalazione_messaggio, null))

                .setPositiveButton(R.string.nuova_segnalazione_conferma, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Messaggi_condomino");


                        descrizioneMessaggio = descrizioneMessaggioE.getText().toString();

                        addMessaggioCondomino(databaseReference, descrizioneMessaggio);


                        dismiss();
                    }
                })

                .setNeutralButton(R.string.nuova_segnalazione_annulla, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {

                        dismiss();
                    }
                });

        return builder.create();

    }

    @Override
    public void onStart() {
        super.onStart();
        TextView testo = (TextView) this.getDialog().findViewById(R.id.textNuovaSegnalazione);
        testo.setText(R.string.nuovo_messaggio);

        descrizioneMessaggioE = (EditText) this.getDialog().findViewById(R.id.textDescrizioneSegnalazione);

        //lettura uid condomino -->  codice fiscale stabile, uid amministratore
        //ricava l'UID dell'utente loggato
        uidCondomino = firebaseAuth.getCurrentUser().getUid().toString();
        //Ricava il riferimento del nodo con UID uidCondomino da firebase
        firebaseDB = FirebaseDB.getCondomini().child(uidCondomino);
        //Prende tutti i child del nodo uidCondomino
        firebaseDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                //Se la chiave del nodo è stabile..
                if(dataSnapshot.getKey().toString().equals("stabile")){

                    //..salva il valore del nodo stabile
                    stabile = dataSnapshot.getValue().toString();

                    //Prende il riferimento del nodo stabile
                    firebaseDB = FirebaseDB.getStabili().child(stabile);
                    //Prende tutti i child del nodo stabile
                    firebaseDB.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                            //Se la chiave è uguale ad amministratore allora...
                            if(dataSnapshot.getKey().toString().equals("amministratore")) {
                                //..salva il valore del nodo amministratore (UID)
                                uidAmministratore = dataSnapshot.getValue().toString();
                            }
                        }

                        @Override
                        public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }


    private void addMessaggioCondomino(DatabaseReference postRef, final String descrizioneMessaggio) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                //Legge il valore del nodo counter
                Integer counter = mutableData.child("counter").getValue(Integer.class);

                if (counter == null) {
                    return Transaction.success(mutableData);
                }

                //Incrementa counter
                counter = counter + 1;

                //Ricava la data e la formatta nel formato appropriato
                Date newDate = new Date(new Date().getTime());
                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm ");
                String stringdate = dt.format(newDate);

                //Instanziamo un nuovo oggetto MessaggioCondomino contenente tutte le informazioni
                //per la creazione di un nuovo nodo Messaggi_condomino su Firebase
                MessaggioCondomino m = new MessaggioCondomino(stringdate,"messaggio", descrizioneMessaggio,uidCondomino,uidAmministratore, stabile);

                //Setta il nome del nodo del messaggio (key)
                mutableData.child(counter.toString()).setValue(m);
                //Setta il counter del nodo Messaggi_condomino
                mutableData.child("counter").setValue(counter);


                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

}