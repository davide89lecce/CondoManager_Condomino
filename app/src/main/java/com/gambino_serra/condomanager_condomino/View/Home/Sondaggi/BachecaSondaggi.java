/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.gambino_serra.condomanager_condomino.View.Home.Sondaggi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.gambino_serra.condomanager_condomino.Model.Entity.Sondaggio;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.View.Home.Sondaggi.DettagliSondaggi.SondaggioRating;
import com.gambino_serra.condomanager_condomino.View.Home.Sondaggi.DettagliSondaggi.SondaggioSceltaMultipla;
import com.gambino_serra.condomanager_condomino.View.Home.Sondaggi.DettagliSondaggi.SondaggioSceltaSingola;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BachecaSondaggi extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";
    String username;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    //private ArrayList<Segnalazione> data;
    public static View.OnClickListener myOnClickListener;
    Context context;
    String condominoNome;
    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String uidCondomino;
    private String stabile;
    Map<String, Object> sondaggioMap;
    ArrayList<Sondaggio> sondaggi;

    private OnFragmentInteractionListener mListener;

    public BachecaSondaggi(){}

    public static BachecaSondaggi newInstance() {
        BachecaSondaggi fragment = new BachecaSondaggi();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bacheca_sondaggi, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onStart() {
        super.onStart();

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        //data = new ArrayList<Segnalazione>();
        sondaggioMap = new HashMap<String, Object>();
        sondaggi = new ArrayList<Sondaggio>();

        myOnClickListener = new MyOnClickListener(context);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        uidCondomino = firebaseAuth.getCurrentUser().getUid().toString();
        firebaseDB = FirebaseDB.getCondomini().child(uidCondomino);

        firebaseDB.child("stabile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ricavo codicefiscale stabile
                stabile = dataSnapshot.getValue().toString();
                Query prova;
                prova = FirebaseDB.getSondaggi().orderByChild("stabile").equalTo(stabile);

                prova.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        sondaggioMap = new HashMap<String,Object>();
                        sondaggioMap.put("id", dataSnapshot.getKey());

                        for ( DataSnapshot child : dataSnapshot.getChildren() ) {
                            sondaggioMap.put(child.getKey(), child.getValue());
                        }

                        //try{

                            final Sondaggio sondaggio = new Sondaggio(
                                    sondaggioMap.get("id").toString(),
                                    sondaggioMap.get("tipologia").toString(),
                                    sondaggioMap.get("oggetto").toString(),
                                    sondaggioMap.get("descrizione").toString(),
                                    sondaggioMap.get("data").toString(),
                                    sondaggioMap.get("stato").toString()
                            );


                            // Controlla se il sondaggio recuperato è aperto
                            if ( sondaggio.getStato().equals("aperto") ) {
                                // Controlla se il condomino in questione ha giaà partecipato al sondaggio
                                Firebase controllo;
                                controllo = FirebaseDB.getSondaggi()
                                        .child(sondaggio.getIdSondaggio())
                                        .child("partecipanti")
                                        .child(uidCondomino);

                                controllo.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //String compilato = dataSnapshot.getValue().toString();

                                        if ( sondaggio.getStato().equals("aperto") && !dataSnapshot.exists()) {
                                            sondaggi.add(sondaggio);

                                            adapter = new AdapterBachecaSondaggi(sondaggi);
                                            recyclerView.setAdapter(adapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                    }
                                });
                            }
                        //}
                        //catch (NullPointerException e) {
                        //    Toast.makeText(getActivity().getApplicationContext(), "Non riesco ad aprire l'oggetto "+ e.toString(), Toast.LENGTH_LONG).show();
                        //}
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) { }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * Menu to allow an interaction in this Menu to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private static class MyOnClickListener extends AppCompatActivity implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            detailsSegnalazione(v);
        }

        private void detailsSegnalazione(View v) {

            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.textViewIdSondaggio);
            String selectedName = (String) textViewName.getText();
            TextView textViewType = (TextView) viewHolder.itemView.findViewById(R.id.D_TipologiaSondaggio);
            String selectedType = (String) textViewType.getText();

            Bundle bundle = new Bundle();
            bundle.putString("idSondaggio", selectedName);

            switch ( selectedType ){
                case "rating":
                {
                    Intent intent = new Intent(context, SondaggioRating.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                }

                case "scelta singola":
                {
                    Intent intent = new Intent(context, SondaggioSceltaSingola.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                }
                case "scelta multipla":
                {
                    Intent intent = new Intent(context, SondaggioSceltaMultipla.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                }
                default:
            }

        }
    }






}
