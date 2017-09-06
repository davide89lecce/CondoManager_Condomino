package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Messaggi;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.gambino_serra.condomanager_condomino.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.Old_View.Utente.BaseActivity;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class DettaglioMessaggio extends BaseActivity {

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

    private FirebaseDB firebaseDB;
    private Firebase dbMessaggi;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mStorage;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    Map<String, Object> MessaggioMap;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_messaggio);

        firebaseAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();

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

                String UID = firebaseAuth.getCurrentUser().getUid().toString();

                // Se nel messaggio è effettivamente presente una foto
                if ( MessaggioMap.get("foto").toString() != "-" ) {
                    showProgressDialog();
                    String filephoto = MessaggioMap.get("foto").toString();
                    // Riferimento al file con cartella Photo e successivamente il nome dell'immagine
                    StorageReference photoRef = mStorage.child("Photo").child( filephoto );


                    // Load the image using Glide
                    Glide.with(getApplicationContext().getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(photoRef)
                            .into(messaggio_foto);


                    //Toast.makeText(getApplicationContext(), "Non riesco ad aprire l'oggetto " + e.toString(), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                    }
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

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            Uri link = data.getData();

            StorageReference filepath = mStorage.child("Photos").child(MessaggioMap.get("foto").toString());

            Picasso.with(getApplicationContext()).load( link ).fit().centerCrop().into(messaggio_foto);
        }


    }*/




    /**
     * Il metodo imposta il messaggio della Dialog.
     */
    @Override
    protected void setMessage() {
        mProgressDialog.setMessage("Caricamento Immagine");
    }
}