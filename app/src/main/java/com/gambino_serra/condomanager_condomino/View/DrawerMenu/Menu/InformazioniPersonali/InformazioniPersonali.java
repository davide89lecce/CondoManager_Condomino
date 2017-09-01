package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.InformazioniPersonali;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.gambino_serra.condomanager_condomino.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.Old_View.Utente.BaseActivity;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;



public class InformazioniPersonali extends Fragment {
    // the Menu initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";      //TODO: che è?
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Oggetti di Layout NUOVI
    TextView mNome;
    TextView mStabile;
    TextView mInterno;
    TextView mTelefono;
    TextView mEmail;


    FirebaseDB firebaseDB;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Query query;


    String UID;
    Map<String, Object> InformazioniMap;

    public InformazioniPersonali() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of this Menu using the provided parameters.
     */
    public static com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori newInstance(String param1, String param2) {
        com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori fragment = new com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Assegna un layout al fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this Menu
        View view = inflater.inflate(R.layout.bacheca_informazioni_personali, container, false);

        mNome = (TextView) view.findViewById(R.id.D_Nome);
        mStabile = (TextView) view.findViewById(R.id.D_Stabile);
        mInterno = (TextView) view.findViewById(R.id.D_Interno);
        mTelefono = (TextView) view.findViewById(R.id.D_Telefono);
        mEmail = (TextView) view.findViewById(R.id.D_Email);

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid().toString();

/*
        mNome = (TextView) findViewById(R.id.D_Nome);
        mStabile = (TextView) findViewById(R.id.D_Stabile);
        mInterno = (TextView) findViewById(R.id.D_Interno);
        mTelefono = (TextView) findViewById(R.id.D_Telefono);
        mEmail = (TextView) findViewById(R.id.D_Email);
*/
    }


    @Override
    public void onStart() {
        super.onStart();

        Firebase condomino = firebaseDB.getCondomini().child(UID);

        InformazioniMap = new HashMap<String, Object>();
        InformazioniMap.put("id", UID);

        condomino.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    InformazioniMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
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


        try {
            mNome.setText(InformazioniMap.get("nome").toString());
            mStabile.setText(InformazioniMap.get("stabile").toString());
            mInterno.setText(InformazioniMap.get("interno").toString());
            mTelefono.setText(InformazioniMap.get("telefono").toString());
            mEmail.setText(InformazioniMap.get("email").toString());
        }catch(NullPointerException e){
            Toast.makeText(getActivity().getApplicationContext(), "Non riesco ad aprire l'oggetto "+ e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
