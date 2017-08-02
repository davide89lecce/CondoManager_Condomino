package com.gambino_serra.condomanager_condomino.View.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.firebase.client.Firebase;
import com.gambino_serra.condomanager_condomino.View.Utente.BaseActivity;
import com.gambino_serra.condomanager_condomino.View.Utente.RegisterAmministratoreActivity;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.gambino_serra.condomanager_condomino.Controller.Login.checkLogin;

public class LoginActivity extends BaseActivity
        implements Response.Listener<String>, Response.ErrorListener {

    //Firebase
    private Firebase DBref;                         //Riferimento al DB
    private FirebaseAuth firebaseAuth;      //Oggetto per l'autenticazione
    private FirebaseUser utente;                    //oggetto per definire l'utente del DB
    private Firebase userRef;       // posso conservare altri riferimenti ad oggetto che punto a piacere


    EditText etUsername, etPassword;
    Button btnLogin;
    Button btnRegister;
    String username;
    String password;
    Response.Listener<String> listener = this;
    Response.ErrorListener errorListener = this;

    private static final String MY_PREFERENCES = "preferences";
    private static final String TIPO_UTENTE = "tipoUtente";
    private static final String LOGGED_USER = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_logo);

        firebaseAuth = FirebaseAuth.getInstance();

        // if(firebaseAuth.getCurrentUser() != null){
        //     //leggere dati e login dell'utente
        // }

        userState();
        //firebaseAuth.signOut();

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            /**
             * Il metodo permette di acquisire i dati inseriti dall'utente, verifica che i campi
             * di testo non siano vuoti ed effettua il login.
             *
             * @param v istanza della View
             */
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString().trim();;
                password = etPassword.getText().toString().trim();;

                showProgressDialog();

                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            hideProgressDialog();
                            /**
                             * Ad operazione effettuata, tramite l'if controllo che l'utente  restituito
                             * non sia null, ovvero che io abbia effettivmento immesso i miei dati
                             */
                            if (firebaseAuth.getCurrentUser() != null) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "LOGIN EFFETTUATO",
                                        Toast.LENGTH_SHORT
                                ).show();
                                // PRENDO IL RIFERIMENTO DELL'UTENTE LOGGATO
                                utente = firebaseAuth.getCurrentUser();

                                //scrittura dati nelle shared e intent a home
                                writeSharedPreferences(username, password, "A");

                                Intent in = new Intent(getApplicationContext(), com.gambino_serra.condomanager_condomino.View.DrawerMenu.activity.MainActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);

                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "DATI NON CORRETTI",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }else{
                            hideProgressDialog();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "DATI NON CORRETTI",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            /**
             * Il metodo permette di accedere alla schermata di registrazione
             * di un nuovo utente.
             * @param v istanza della View
             */
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), RegisterAmministratoreActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

            }
        });

    }

    /**
     * Il metodo imposta il messaggio della Dialog.
     */
    @Override
    protected void setMessage() {
        mProgressDialog.setMessage(getString(R.string.login));
    }

    /**
     * Il metodo e' invocato alla risposta (dati ricevuti da database altervista) della richiesta di autenticazione.
     * @param response
     */
    @Override
    public void onResponse(String response) {

        hideProgressDialog();
        checkLogin(response, getApplicationContext(),username);
    }

    /**
     * Il metodo viene invocato in caso di problemi nella ricezione della risposta.
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    /**
     *  Il metodo verifica che le SharedPreferences contengano dati, nel caso contrario l'utente risulterà non connesso.
     */
    public void userState(){

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        if (!sharedPrefs.getAll().isEmpty()) {
            getStatusAndGoHome();
        }
    }

    /**
     * Il metodo verifica il tipo di utente e lo indirizza nella sua Home Activity.
     */
    private void getStatusAndGoHome() {

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

        if (sharedPrefs.getString(TIPO_UTENTE, "").equals("A")) {
            Intent in = new Intent(this, com.gambino_serra.condomanager_condomino.View.DrawerMenu.activity.MainActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
    }

    private void writeSharedPreferences(String username, String password, String tipo_utente){

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPrefs.edit();
        editor.putString(TIPO_UTENTE,tipo_utente);
        editor.putString(LOGGED_USER,username);
        editor.apply();
    }
}
