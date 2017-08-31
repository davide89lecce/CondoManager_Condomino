package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Interventi;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.NodeUtilities;
import com.gambino_serra.condomanager_condomino.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.Old_View.Condomino.Dialog.DialogChiamaAmministratore;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by condomanager_condomino on 06/03/17.
 */

public class DettaglioIntervento extends AppCompatActivity {

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

    String idTicket;

    // Oggetti di Layout NUOVI
    TextView mOggetto;
    TextView mDescrizione;
    TextView mStato;
    TextView mUltimoAggiornamento;
    TextView mFornitore;
    TextView mDataAgg;


    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Map<String, Object> ticketInterventoMap;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_intervento);

        firebaseAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            idTicket = bundle.get("idSegnalazione").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("idSegnalazione", idTicket); //TODO: segnalazione o ticket???
            editor.apply();

        } else {
            //TODO: perchè
            idTicket = sharedPrefs.getString("idSegnalazione", "").toString();

            bundle = new Bundle();
            bundle.putString("idSegnalazione", idTicket);

        }

        // Avvaloro i nuovi rierimenti al layout
        mOggetto = (TextView) findViewById(R.id.D_Oggetto);
        mDescrizione = (TextView) findViewById(R.id.D_Descrizione);
        mStato = (TextView) findViewById(R.id.D_Stato);
        mUltimoAggiornamento = (TextView) findViewById(R.id.D_UltimoAggiornamento);
        mDataAgg = (TextView) findViewById(R.id.D_dataAggiorn);
        mFornitore = (TextView) findViewById(R.id.D_Fornitore);


        Query intervento;
        intervento = FirebaseDB.getInterventi().orderByKey().equalTo(idTicket);

        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ticketInterventoMap = new HashMap<String, Object>();
                // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
                ticketInterventoMap.put("idIntervento", idTicket);


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ticketInterventoMap.put(child.getKey(), child.getValue());
                }

                TicketIntervento ticketIntervento = new TicketIntervento(
                        ticketInterventoMap.get("idIntervento").toString(),
                        ticketInterventoMap.get("amministratore").toString(),
                        ticketInterventoMap.get("data_ticket").toString(),
                        ticketInterventoMap.get("data_ultimo_aggiornamento").toString(),
                        ticketInterventoMap.get("fornitore").toString(),
                        ticketInterventoMap.get("messaggio_condomino").toString(),
                        ticketInterventoMap.get("aggiornamento_condomini").toString(),
                        ticketInterventoMap.get("descrizione_condomini").toString(),
                        ticketInterventoMap.get("oggetto").toString(),
                        ticketInterventoMap.get("rapporti_intervento").toString(),
                        ticketInterventoMap.get("richiesta").toString(),
                        ticketInterventoMap.get("stabile").toString(),
                        ticketInterventoMap.get("stato").toString(),
                        ticketInterventoMap.get("priorità").toString()
                );

                mOggetto.setText(ticketIntervento.getOggetto());
                mFornitore.setText(ticketIntervento.getFornitore());
                mDataAgg.setText(ticketIntervento.getDataUltimoAggiornamento());
                mUltimoAggiornamento.setText(ticketIntervento.getAggiornamentoCondomini());
                mDescrizione.setText(ticketIntervento.getDescrizioneCondomini());
                mStato.setText(ticketIntervento.getStato());

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

        /*
        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ticketInterventoMap = new HashMap<String, Object>();
                // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
                ticketInterventoMap.put("id", dataSnapshot.getKey());

                // per ognuna delle coppie chiave valore, inseriamo il corrispondente elemento nel map creato
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ticketInterventoMap.put(child.getKey(), child.getValue());
                }

                TicketIntervento ticketIntervento = new TicketIntervento(
                        ticketInterventoMap.get("id").toString(),
                        ticketInterventoMap.get("amministratore").toString(),
                        ticketInterventoMap.get("data_ticket").toString(),
                        ticketInterventoMap.get("data_ultimo_aggiornamento").toString(),
                        ticketInterventoMap.get("fornitore").toString(),
                        ticketInterventoMap.get("messaggio_condomino").toString(),
                        ticketInterventoMap.get("descrizione_condomini").toString(),
                        ticketInterventoMap.get("oggetto").toString(),
                        ticketInterventoMap.get("priorità").toString(),
                        ticketInterventoMap.get("rapporti_intervento").toString(),
                        ticketInterventoMap.get("richiesta").toString(),
                        ticketInterventoMap.get("stabile").toString(),
                        ticketInterventoMap.get("stato").toString());

                if(ticketIntervento.getStato().equals("A")){
                    descrizioneStatoT.setText("Questa richiesta è in attesa di essere presa in carico");
                    imageStatoI.setImageResource(R.drawable.sand_clock2);
                }else if(ticketIntervento.getStato().equals("B") || stato.equals("C") || stato.equals("D")){
                    descrizioneStatoT.setText("Questa richiesta è in corso d'opera");
                    imageStatoI.setImageResource(R.drawable.wrench);
                }else if(ticketIntervento.getStato().equals("E") || stato.equals("F")){
                    descrizioneStatoT.setText("I lavori per questo intervento sono stati conclusi");
                    imageStatoI.setImageResource(R.drawable.checked);
                }else if(ticketIntervento.getStato().equals("G")){
                    descrizioneStatoT.setText("Questa richiesta è stata rifiutata.\nContattare l'amministratore per maggiori dettagli");
                    imageStatoI.setImageResource(R.drawable.error);
                }

                dataT.setText(ticketIntervento.getDataTicket());
                segnalazioneT.setText(ticketIntervento.getOggetto());
                condominioT.setText(ticketIntervento.getDataUltimoAggiornamento());
                descrizioneStatoT.setText(ticketIntervento.getMessaggioCondomino());

                //final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPrefs.edit();
                //editor.putString("data", ticketIntervento.getDataTicket());
                //editor.putString("segnalazione", ticketIntervento.getOggetto());
                //editor.putString("condomino", usernameCondomino);
                //editor.putString("idCondominio", idCondominio);
                //editor.putString("fornitore", fornitore);
                //editor.putString("stato", stato);
                //editor.apply();


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
*/

    }


}