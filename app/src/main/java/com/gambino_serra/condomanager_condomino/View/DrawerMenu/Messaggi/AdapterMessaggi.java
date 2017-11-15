package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Messaggi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambino_serra.condomanager_condomino.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_condomino.tesi.R;

import java.util.ArrayList;


public class AdapterMessaggi extends RecyclerView.Adapter<AdapterMessaggi.MyViewHolder> {

    private ArrayList<MessaggioCondomino> dataset;
    private Context context;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView DataMessaggio;
        TextView TestoMessaggio;
        TextView TipologiaMessaggio;
        ImageView imageViewMessaggio;
        ImageView mLogoTipo;
        ImageView mBackTipo;
        ImageView mCircle;
        TextView textViewIdSegnalazione;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageViewMessaggio = (ImageView) itemView.findViewById(R.id.imageViewMessaggio);
            this.TestoMessaggio = (TextView) itemView.findViewById(R.id.TestoMessaggio);
            this.TipologiaMessaggio = (TextView) itemView.findViewById(R.id.TipologiaMessaggio);
            this.DataMessaggio = (TextView) itemView.findViewById(R.id.DataMessaggio);
            this.mLogoTipo = (ImageView) itemView.findViewById(R.id.imageViewMessaggio);
            this.mBackTipo = (ImageView) itemView.findViewById(R.id.imageView);
            this.mCircle = (ImageView) itemView.findViewById(R.id.imageView2);
            this.textViewIdSegnalazione = (TextView) itemView.findViewById(R.id.textViewIdSegnalazione);
        }
    }

    public AdapterMessaggi(ArrayList<MessaggioCondomino> dataset , Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_messaggio_bacheca, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaMessaggi.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView TipologiaMessaggio = holder.TipologiaMessaggio;
        TextView TestoMessaggio = holder.TestoMessaggio;
        TextView DataMessaggio = holder.DataMessaggio;
        ImageView imageViewMessaggio = holder.imageViewMessaggio;
        ImageView mLogoTipo = holder.mLogoTipo;
        ImageView mBackTipo = holder.mBackTipo;
        ImageView mCircle = holder.mCircle;
        TextView textViewIdSegnalazione = holder.textViewIdSegnalazione;

        TipologiaMessaggio.setText( dataset.get(listPosition).getTipologia());
        TestoMessaggio.setText( dataset.get(listPosition).getMessaggio());
        DataMessaggio.setText(dataset.get(listPosition).getData());
        textViewIdSegnalazione.setText(dataset.get(listPosition).getId().toString());


        int messColor = context.getResources().getColor(R.color.colorMess);
        int segnColor = context.getResources().getColor(R.color.colorSegnalaz);
        Drawable blue_msg  = context.getResources().getDrawable(R.drawable.blue_msg);
        Drawable red_msg  = context.getResources().getDrawable(R.drawable.red_msg);

        String tipo = dataset.get(listPosition).getTipologia();

        switch(tipo) {

            // intervento richiesto o rifiutato (al condomino interressa solo che sia stato processato
            // dall'amministratore, se un fornitore lo rifiuterà, lui lo vedrà ancora in attesa
            // di essere preso in carico
            case "Messaggio" :
            {
                mBackTipo.setColorFilter( messColor );
                mCircle.setColorFilter( messColor);
                mLogoTipo.setImageDrawable(blue_msg);
                break;
            }

            case "Segnalazione":
            {
                mBackTipo.setColorFilter( segnColor );
                mCircle.setColorFilter( segnColor );
                mLogoTipo.setImageDrawable(red_msg);
                break;
            }
            default:
        }



    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}