package com.gambino_serra.condomanager_condomino.View.NuovaSegnalazione;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

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

                        //firebaseDB = FirebaseDB.getFirebase();
                        //firebaseDB = firebaseDB.child("Segnalazione").child("Counter");
                        //if(firebaseDB.getValue() == null) {
                        //firebaseDB.setValue(1);
                       // } else {
                        //firebaseDB.setValue((Long) firebaseDB.getValue() + 1);
                        //}
                     //   firebaseDB = FirebaseDB.getFirebase();
                     //   firebaseDB = firebaseDB.child("Segnalazione");
                      //  firebaseDB.child("Segnalazione").setValue("");
                      //  firebaseDB.child("Condomino").setValue("");
                      //  firebaseDB.child("Condominio").setValue("");
                      //  firebaseDB.child("Fornitore").setValue("");
                      //  firebaseDB.child("Stato").setValue("");

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Segnalazioni").child("Counter"); //FirebaseDatabase.getInstance().getReference();
                        segnalazioneIncrementCounter(databaseReference);

                        descrizioneSegnalazione = descrizioneSegnalazioneE.getText().toString().replace("'", "\\'");

                        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putString("descrizioneSegnalazione", descrizioneSegnalazione);
                        editor.apply();

                       // HTTPRequestCondominio.getCondominioFromCondomino(username, ((CondominoHomeActivity) getActivity()), ((CondominoHomeActivity) getActivity()), ((CondominoHomeActivity) getActivity()));

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


    private void segnalazioneIncrementCounter(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                //
                // Segnalazione s = mutableData.getValue(Segnalazione.class);
                Integer counter = mutableData.getValue(Integer.class);
                if (counter == null) {
                    return Transaction.success(mutableData);
                }

                //if (s.counter.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                   // p.starCount = p.starCount - 1;
                   // p.stars.remove(getUid());
               // } else {
                    // Star the post and add self to stars
                    //s.setIdSegnalazione(s.getIdSegnalazione() + 1);
                    //s.counter.put(getUid(), true);
               // }
                counter = counter + 1;
                // Set value and report transaction success
                mutableData.setValue(counter);
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