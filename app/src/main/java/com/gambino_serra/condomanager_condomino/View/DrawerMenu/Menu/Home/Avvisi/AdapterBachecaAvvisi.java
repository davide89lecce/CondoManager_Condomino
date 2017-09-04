package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Avvisi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambino_serra.condomanager_condomino.Model.Entity.Avviso;
import com.gambino_serra.condomanager_condomino.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Interventi.AdapterBachecaInterventi;
import com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Interventi.BachecaInterventi;
import com.gambino_serra.condomanager_condomino.tesi.R;

import java.util.ArrayList;

import static com.gambino_serra.condomanager_condomino.tesi.R.id.Logo_Interv;


public class AdapterBachecaAvvisi extends RecyclerView.Adapter<AdapterBachecaAvvisi.MyViewHolder> {

    private ArrayList<Avviso> dataset;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mOggetto;
        TextView mDescrizione;
        TextView IdAvviso;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mOggetto = (TextView) itemView.findViewById(R.id.Oggetto_Avviso);
            this.mDescrizione = (TextView) itemView.findViewById(R.id.Descr_Avviso);
            //Campo nascosto per recuperare il riferimento
            this.IdAvviso = (TextView) itemView.findViewById(R.id.IDAvviso);
        }
    }

    public AdapterBachecaAvvisi(ArrayList<Avviso> dataset) {
        this.dataset = dataset;
    }

    public AdapterBachecaAvvisi.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_avviso_bacheca, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaAvvisi.myOnClickListener);

        AdapterBachecaAvvisi.MyViewHolder myViewHolder = new AdapterBachecaAvvisi.MyViewHolder(view);
        return myViewHolder;
    }

    public void onBindViewHolder(final AdapterBachecaAvvisi.MyViewHolder holder, final int listPosition) {

        TextView mOggetto = holder.mOggetto;
        TextView mDescrizione = holder.mDescrizione;
        TextView IdAvviso = holder.IdAvviso;


        mOggetto.setText(dataset.get(listPosition).getOggetto());
        mDescrizione.setText(dataset.get(listPosition).getDescrizione()); //TODO : stampa solo anterima descrizione
        IdAvviso.setText(dataset.get(listPosition).getIdAvviso());


    }

    public int getItemCount() {
        return dataset.size();
    }

}

