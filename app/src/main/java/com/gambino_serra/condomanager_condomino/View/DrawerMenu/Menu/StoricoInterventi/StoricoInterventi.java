package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.StoricoInterventi;

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
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.gambino_serra.condomanager_condomino.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Interventi.AdapterBachecaInterventi;
import com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Interventi.BachecaInterventi;
import com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Interventi.DettaglioIntervento;
import com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.ListaFornitori.DettaglioFornitore;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StoricoInterventi extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String uidCondomino;
    private String stabile;
    Map<String, Object> ticketInterventoMap;
    ArrayList<TicketIntervento> interventi;
    Context context;
    private OnFragmentInteractionListener mListener;

    public StoricoInterventi() { }

    public static StoricoInterventi newInstance(String param1, String param2) {
        StoricoInterventi fragment = new StoricoInterventi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bacheca_storico_interventi, container, false);
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
        ticketInterventoMap = new HashMap<String,Object>();
        interventi = new ArrayList<TicketIntervento>();

        myOnClickListener = new MyOnClickListener(context);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //lettura uid condomino -->  codice fiscale stabile, uid amministratore
        uidCondomino = firebaseAuth.getCurrentUser().getUid().toString();
        firebaseDB = FirebaseDB.getCondomini().child(uidCondomino);


        firebaseDB.child("stabile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ricavo codicefiscale stabile
                stabile = dataSnapshot.getValue().toString();
                Query prova;
                prova = FirebaseDB.getInterventi().orderByChild("stabile").equalTo(stabile);

                prova.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        ticketInterventoMap = new HashMap<String,Object>();
                        ticketInterventoMap.put("id", dataSnapshot.getKey());

                        for ( DataSnapshot child : dataSnapshot.getChildren() ) {
                            ticketInterventoMap.put(child.getKey(), child.getValue());
                            }

                        try{
                            TicketIntervento ticketIntervento = new TicketIntervento(
                                    ticketInterventoMap.get("id").toString(),
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
                                    ticketInterventoMap.get("stato").toString() ,
                                    ticketInterventoMap.get("priorità").toString(),
                                    "ciao","ciao","ciao","ciao");//TODO

                            interventi.add(ticketIntervento);
                        }
                        catch (NullPointerException e)
                            {
                            Toast.makeText(getActivity().getApplicationContext(), "Non riesco ad aprire l'oggetto "+ e.toString(), Toast.LENGTH_LONG).show();
                            }

                        adapter = new AdapterBachecaInterventi(interventi);
                        recyclerView.setAdapter(adapter);
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

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
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
            detailsIntervento(v);
            }

        private void detailsIntervento(View v) {

            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.IDTicket);
            String selectedName = (String) textViewName.getText();

            Bundle bundle = new Bundle();
            bundle.putString("idTicket", selectedName);

            Intent intent = new Intent(context, DettaglioIntervento.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}