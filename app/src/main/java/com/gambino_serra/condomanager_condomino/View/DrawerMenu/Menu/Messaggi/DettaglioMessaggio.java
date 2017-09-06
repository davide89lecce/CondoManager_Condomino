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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
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
                    //StorageReference storageRef = mStorage.child( "Photo/CondomanagerPhoto30082017_095657.jpg" );



                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Photo/CondomanagerPhoto30082017_095657.jpg");




                    String downloadUri = storageRef.getPath();

                            //getDownloadUrl().getResult();

                    Log.d ("HEY", downloadUri.toString());
                    /*
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(getApplicationContext()).load(   uri   ).fit().into(messaggio_foto);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "Non riesco ad aprire l'oggetto " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    */

                    //Uri uriii =  "gs://condomanager-a5aa6.appspot.com/Photo/CondomanagerPhoto30082017_110031.jpg" ;

                    //https://firebasestorage.googleapis.com/v0/b/condomanager-a5aa6.appspot.com/o/Photo%2FCondomanagerPhoto30082017_095657.jpg?alt=media&token=c047ca82-0a03-4156-9f0f-f4a93ca34b26

                    Picasso.with(getApplicationContext()).load(
                            storageRef.getPath()
                            //"https://firebasestorage.googleapis.com/v0/b/condomanager-a5aa6.appspot.com/o/Photo%2FCondomanagerPhoto30082017_095657.jpg?alt=media&token=c047ca82-0a03-4156-9f0f-f4a93ca34b26"

                       ).fit().into(messaggio_foto);




                    hideProgressDialog();
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


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