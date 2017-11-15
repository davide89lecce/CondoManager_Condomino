package com.gambino_serra.condomanager_condomino.View.Home;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gambino_serra.condomanager_condomino.View.Home.Avvisi.BachecaAvvisi;
import com.gambino_serra.condomanager_condomino.View.Home.Interventi.BachecaInterventi;
import com.gambino_serra.condomanager_condomino.View.Home.Sondaggi.BachecaSondaggi;
import com.gambino_serra.condomanager_condomino.View.NuovaSegnalazione.DialogNuovaSegnalazione;
import com.gambino_serra.condomanager_condomino.View.NuovoMessaggio.DialogNuovoMessaggio;
import com.gambino_serra.condomanager_condomino.tesi.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


public class Home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView TitoloSezione;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    FrameLayout fl;

    private String mParam1;
    private String mParam2;

    BottomNavigationView bottomNavigationView;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this Menu using the provided parameters.
     */
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        // Inflate the layout for this Menu
        return inflater.inflate(R.layout.activity_main_navigationbar, container, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Fragment childFragment = new Home();
        //FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //transaction.replace(R.id.frame_layout, childFragment).commit();

        TitoloSezione = (TextView) getActivity().findViewById(R.id.D_SezioneTab);

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = BachecaSondaggi.newInstance();
                                TitoloSezione.setText("BACHECA SONDAGGI");
                                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                                bottomNavigationView.getMenu().getItem(1).setChecked(false);
                                bottomNavigationView.getMenu().getItem(2).setChecked(false);
                                break;
                            case R.id.action_item2:
                                selectedFragment = BachecaAvvisi.newInstance();
                                TitoloSezione.setText("BACHECA AVVISI");
                                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                                bottomNavigationView.getMenu().getItem(2).setChecked(false);
                                break;
                            case R.id.action_item3:
                                selectedFragment = BachecaInterventi.newInstance();
                                TitoloSezione.setText("BACHECA INTERVENTI");
                                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                bottomNavigationView.getMenu().getItem(1).setChecked(false);
                                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                                break;
                        }
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();

                        return true;
                    }
                });
// TODO : cerca animazione per tab da sx a dx
        //Manually displaying the first Menu - one time only
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, BachecaAvvisi.newInstance());
        transaction.commit();
        //Seleziona l'item interventi in corso
        bottomNavigationView.getMenu().getItem(0).setChecked(false);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.getMenu().getItem(2).setChecked(false);
        TitoloSezione.setText("BACHECA AVVISI");

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
        //ListView lv = (ListView) view.findViewById(R.id.lvSome);
        //lv.setAdapter(adapter);

        //Floating button
        materialDesignFAM = (FloatingActionMenu) getActivity().findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) getActivity().findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) getActivity().findViewById(R.id.material_design_floating_action_menu_item2);

        materialDesignFAM.hideMenu(true);
        materialDesignFAM.setClosedOnTouchOutside(true);
        materialDesignFAM.showMenu(true);
        fl = (FrameLayout)view.findViewById(R.id.frame_layout);

//        materialDesignFAM.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP){
//
//                    if(materialDesignFAM.isOpened())
//                        { fl.setBackgroundColor(Color.GRAY); }
//                    else
//                        { fl.setBackgroundColor(Color.TRANSPARENT); }
//                    return true;
//                }
//                return true; // consume the event
//            }
//        });


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DialogFragment newFragment = new DialogNuovaSegnalazione();
                newFragment.show(getActivity().getFragmentManager(), "NuovaSegnalazione");
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DialogFragment newFragment = new DialogNuovoMessaggio();
                newFragment.show(getActivity().getFragmentManager(), "NuovoMessaggio");
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                materialDesignFAM.close(true);
            }
        });

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this Menu to allow an interaction in this Menu
     * to be communicated to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
