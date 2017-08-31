package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Home.Interventi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambino_serra.condomanager_condomino.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_condomino.tesi.R;

import java.util.ArrayList;

import static com.gambino_serra.condomanager_condomino.tesi.R.id.Logo_Interv;
import static com.gambino_serra.condomanager_condomino.tesi.R.id.TipologiaMessaggio;


public class AdapterBachecaInterventi extends RecyclerView.Adapter<AdapterBachecaInterventi.MyViewHolder> {

    private ArrayList<TicketIntervento> dataset;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mOggetto;
        TextView mStato;
        TextView mDataAgg;
        TextView mAgg;
        ImageView mLogoStato;
        TextView IdTicket;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mOggetto = (TextView) itemView.findViewById(R.id.Oggetto_Interv);
            this.mStato = (TextView) itemView.findViewById(R.id.Stato_Interv);
            this.mDataAgg = (TextView) itemView.findViewById(R.id.DataAgg_Interv);
            this.mAgg = (TextView) itemView.findViewById(R.id.Aggiornamento_Interv);
            this.mLogoStato = (ImageView) itemView.findViewById(Logo_Interv);
            //Campo nascosto per recuperare il riferimento
            this.IdTicket = (TextView) itemView.findViewById(R.id.IDTicket);
        }
    }

    public AdapterBachecaInterventi(ArrayList<TicketIntervento> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_intervento_bacheca, parent, false);

        //Setta l'onclick sulla recycler view presente nella classe Interventi
        view.setOnClickListener(BachecaInterventi.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView mOggetto = holder.mOggetto;
        TextView mStato = holder.mStato;
        TextView mDataAgg = holder.mDataAgg;
        TextView mAgg = holder.mAgg;
        ImageView mLogoStato = holder.mLogoStato;
        TextView IdTicket = holder.IdTicket;


        mOggetto.setText(dataset.get(listPosition).getOggetto());
        mStato.setText(dataset.get(listPosition).getStato());
        mDataAgg.setText(dataset.get(listPosition).getDataUltimoAggiornamento());
        mAgg.setText(dataset.get(listPosition).getAggiornamentoCondomini());
        IdTicket.setText(dataset.get(listPosition).getIdTicketIntervento());
        //textViewSegnalazione.setText( dataset.get(listPosition).getOggetto());


        String stato = dataset.get(listPosition).getStato();

        switch(stato) {

            // intervento richiesto o rifiutato (al condomino interressa solo che sia stato processato
            // dall'amministratore, se un fornitore lo rifiuterà, lui lo vedrà ancora in attesa
            // di essere preso in carico
            case "A" :
            case "D" :
                {
                mLogoStato.setImageResource(R.drawable.tool_blue);
                break;
                }

            case "B": // intervento in corso
                {
                mLogoStato.setImageResource(R.drawable.tool_orange);
                break;
                }
            case "C":   // intervento concluso
                {
                mLogoStato.setImageResource(R.drawable.tool_green);
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
