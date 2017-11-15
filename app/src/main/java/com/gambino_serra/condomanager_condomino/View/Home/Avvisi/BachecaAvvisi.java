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

package com.gambino_serra.condomanager_condomino.View.Home.Avvisi;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.gambino_serra.condomanager_condomino.Model.Entity.Avviso;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BachecaAvvisi extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    Context context;

    private Firebase firebaseDB;
    private FirebaseAuth firebaseAuth;
    private SimpleDateFormat today;

    private String uidCondomino;
    private String stabile;
    Map<String, Object> avvisoMap;
    ArrayList<Avviso> avvisi;


    public static BachecaAvvisi newInstance() {
        BachecaAvvisi fragment = new BachecaAvvisi();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_bacheca_avvisi, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        avvisoMap = new HashMap<String,Object>();
        avvisi = new ArrayList<Avviso>();

        //myOnClickListener = new BachecaAvvisi.MyOnClickListener(context);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //lettura uid condomino -->  codice fiscale stabile, uid amministratore
        uidCondomino = firebaseAuth.getCurrentUser().getUid().toString();
        firebaseDB = FirebaseDB.getCondomini().child(uidCondomino);

        //today = DataFormat(); TODO: salva la data odiarna per visualizzare solo gli avvisi non scaduti


        firebaseDB.child("stabile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ricavo codicefiscale stabile
                stabile = dataSnapshot.getValue().toString();
                Query prova;
                prova = FirebaseDB.getAvvisi().orderByChild("stabile").equalTo(stabile);

                prova.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        avvisoMap = new HashMap<String,Object>();
                        avvisoMap.put("id", dataSnapshot.getKey());

                        for ( DataSnapshot child : dataSnapshot.getChildren() ) {
                            avvisoMap.put(child.getKey(), child.getValue());
                        }



                            Avviso avviso = new Avviso(
                                    avvisoMap.get("id").toString(),
                                    avvisoMap.get("amministratore").toString(),
                                    avvisoMap.get("stabile").toString(),
                                    avvisoMap.get("oggetto").toString(),
                                    avvisoMap.get("descrizione").toString(),
                                    avvisoMap.get("scadenza").toString(),
                                    avvisoMap.get("tipologia").toString()
                            );


                            //Ricava la data attuale e la formatta nel formato appropriato
                            Date dataAttuale = new Date(new Date().getTime());

                            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            Date dataAvviso = new Date();

                            try {
                                //Parse di dataAvviso in Date
                                dataAvviso = dt.parse(avviso.getDataScadenza());
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }

                            //Confronto di data scadenza avviso con data attuale
                            if (dataAttuale.getTime() < dataAvviso.getTime()) {
                                // AGGIUNGE L'AVVISO NELLA STRUTTURA CONTENENTE TUTTI GLI AVVISI
                                avvisi.add(avviso);
                            }

                            // AGGIUNGE LA STRUTTURA "AVVISI" NELL'ADAPTER PER VISUALIZZARLO NELLA RECYCLER
                            //Effettua l'ordinamento degli avvisi per importanza
                            Collections.sort(avvisi, new Comparator<Avviso>() {
                                @Override
                                public int compare(Avviso avviso1, Avviso avviso2) {
                                    return avviso1.compareTo(avviso2);
                                }
                            });




                            adapter = new AdapterBachecaAvvisi(avvisi);
                        recyclerView.setAdapter(adapter);
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
