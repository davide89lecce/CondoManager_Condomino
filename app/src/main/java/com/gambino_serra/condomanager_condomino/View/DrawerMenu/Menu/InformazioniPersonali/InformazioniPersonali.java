package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.InformazioniPersonali;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.gambino_serra.condomanager_condomino.Model.Entity.Condomino;
import com.gambino_serra.condomanager_condomino.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.Map;


public class InformazioniPersonali extends Fragment {
    // the Menu initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView mNome;
    TextView mCodiceFiscale;
    TextView mStabile;
    TextView mInterno;
    TextView mTelefono;
    TextView mEmail;
    String uidCondomino;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    Map<String, Object> InformazioniMap;

    public InformazioniPersonali() { }

    /**
     * Use this factory method to create a new instance of this Menu using the provided parameters.
     */
    public static com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.InformazioniPersonali.InformazioniPersonali newInstance(String param1, String param2) {
        com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.InformazioniPersonali.InformazioniPersonali fragment = new com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.InformazioniPersonali.InformazioniPersonali();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this Menu
        View view = inflater.inflate(R.layout.bacheca_informazioni_personali, container, false);
        mNome = (TextView) view.findViewById(R.id.Descr_Avviso);
        mCodiceFiscale = (TextView) view.findViewById(R.id.D_CodiceFiscale);
        mStabile = (TextView) view.findViewById(R.id.D_Stabile);
        mInterno = (TextView) view.findViewById(R.id.D_Interno);
        mTelefono = (TextView) view.findViewById(R.id.D_Telefono);
        mEmail = (TextView) view.findViewById(R.id.D_Email);
        return view;
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uidCondomino = firebaseUser.getUid().toString();

        com.firebase.client.Query prova;
        prova = FirebaseDB.getCondomini().orderByKey().equalTo(uidCondomino);
        prova.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                InformazioniMap = new HashMap<String, Object>();
                InformazioniMap.put("uidCondomino", dataSnapshot.getKey());

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    InformazioniMap.put(child.getKey(), child.getValue());
                    }

                Condomino condomino = new Condomino(
                        InformazioniMap.get("nome").toString(),
                        InformazioniMap.get("codicefiscale").toString(),
                        InformazioniMap.get("email").toString(),
                        InformazioniMap.get("telefono").toString(),
                        InformazioniMap.get("stabile").toString(),
                        InformazioniMap.get("interno").toString() );

                mNome.setText(condomino.getNome());
                mCodiceFiscale.setText(condomino.getCodice_fiscale());
                mStabile.setText(condomino.getStabile());
                mInterno.setText(condomino.getInterno());
                mTelefono.setText(condomino.getTelefono());
                mEmail.setText(condomino.getEmail());
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
}