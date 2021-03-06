package com.gambino_serra.condomanager_condomino.View.Home.Avvisi;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambino_serra.condomanager_condomino.Model.Entity.Avviso;
import com.gambino_serra.condomanager_condomino.tesi.R;

import java.util.ArrayList;


public class AdapterBachecaAvvisi extends RecyclerView.Adapter<AdapterBachecaAvvisi.MyViewHolder> {

    private ArrayList<Avviso> dataset;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mOggetto;
        TextView mDescrizione;
        TextView IdAvviso;
        ImageView bg1;
        ImageView bg2;
        ImageView logoAvviso;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mOggetto = (TextView) itemView.findViewById(R.id.Oggetto_Avviso);
            this.mDescrizione = (TextView) itemView.findViewById(R.id.Descr_Avviso);
            //Campo nascosto per recuperare il riferimento
            this.IdAvviso = (TextView) itemView.findViewById(R.id.IDAvviso);
            this.bg1 = (ImageView) itemView.findViewById(R.id.imageView);
            this.bg2 = (ImageView) itemView.findViewById(R.id.imageView2);
            this.logoAvviso = (ImageView) itemView.findViewById(R.id.Logo_Interv);
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
        ImageView bg1 = holder.bg1;
        ImageView bg2 = holder.bg2;
        ImageView logoAvviso = holder.logoAvviso;


        mOggetto.setText(dataset.get(listPosition).getOggetto());
        mDescrizione.setText(dataset.get(listPosition).getDescrizione());
        IdAvviso.setText(dataset.get(listPosition).getIdAvviso());


        String tipologia = dataset.get(listPosition).getTipologia();

        switch (tipologia){

            case "standard":
                bg1.setColorFilter( Color.parseColor("#ffff8800") );
                bg2.setColorFilter( Color.parseColor("#ffff8800"));
                logoAvviso.setImageResource(R.drawable.warn_yel);

                break;
            case "importante":
                bg1.setColorFilter( Color.parseColor("#ffcc0000") );
                bg2.setColorFilter( Color.parseColor("#ffcc0000"));
                logoAvviso.setImageResource(R.drawable.warn_red);
                break;

            default:
        }

    }

    public int getItemCount() {
        return dataset.size();
    }

}

