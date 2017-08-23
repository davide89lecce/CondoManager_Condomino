package com.gambino_serra.condomanager_condomino.View.NuovaSegnalazione;

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

import com.firebase.client.Firebase;
import com.gambino_serra.condomanager_condomino.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class DialogNuovaSegnalazione extends DialogFragment {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    private Firebase firebaseDB;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    EditText descrizioneSegnalazioneE;
    String descrizioneSegnalazione;
    String username;

    public DialogNuovaSegnalazione() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        TextView title = new TextView(getActivity());
        title.setText(R.string.title_nuova_segnalazione);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(30);
        title.setBackgroundResource(R.color.primarySegnalazione);
        title.setTextColor(Color.WHITE);
        builder.setCustomTitle(title);

        builder.setView(inflater.inflate(R.layout.dialog_nuova_segnalazione_old, null))

                .setPositiveButton(R.string.nuova_segnalazione_conferma, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Messaggi");


                        descrizioneSegnalazione = descrizioneSegnalazioneE.getText().toString();

                        addMessaggioCondomino(databaseReference,descrizioneSegnalazione);


                        // DA CHIEDERE SE SERVE ANCORA
                        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putString("descrizioneSegnalazione", descrizioneSegnalazione);
                        editor.apply();

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
        testo.setText(R.string.nuova_segnalazione);

        descrizioneSegnalazioneE = (EditText) this.getDialog().findViewById(R.id.textDescrizioneSegnalazione);

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


    private void addMessaggioCondomino(DatabaseReference postRef, final String descrizioneSegnalazione) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                Integer counter = mutableData.child("Counter").getValue(Integer.class);
                if (counter == null) {
                    return Transaction.success(mutableData);
                }

                counter = counter + 1;


                // SETTIAMO INIZIALMENT LA DATA A 0 PER POI ANDARLA AD INSERIRE COME CHILD SINGOLO
                // E TENERLA AGGIORNATA CON LA DATA PRECISA DI INSERIMENTO DEL MSG NEL DB
                MessaggioCondomino m = new MessaggioCondomino(counter, "0","Segnalazione",descrizioneSegnalazione,2,3);
                // Set value and report transaction success

                //Firebase legge le coppie chiave-valore tramite i metodi get della classe di appartenenza dell'oggetto
                mutableData.child("Messaggio"+counter).setValue(m);
                mutableData.child("Counter").setValue(counter);


                //PER INSERIRE LA DATA NEL FORMATO CORRETTO
                Date newDate = new Date(new Date().getTime());
                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm ");
                String stringdate = dt.format(newDate);

                mutableData.child("Messaggio"+counter).child("data").setValue(stringdate);
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