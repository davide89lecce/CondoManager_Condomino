package com.gambino_serra.condomanager_condomino.View.DrawerMenu.Menu.Messaggi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gambino_serra.condomanager_condomino.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_condomino.tesi.R;
import java.util.ArrayList;
import static com.gambino_serra.condomanager_condomino.tesi.R.id.imageView;


public class AdapterMessaggi extends RecyclerView.Adapter<AdapterMessaggi.MyViewHolder> {

    private ArrayList<MessaggioCondomino> dataset;

    int row;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView DataMessaggio;
        TextView TestoMessaggio;
        TextView TipologiaMessaggio;
        ImageView imageViewMessaggio;
        TextView textViewIdSegnalazione;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageViewMessaggio = (ImageView) itemView.findViewById(R.id.imageViewMessaggio);
            this.TestoMessaggio = (TextView) itemView.findViewById(R.id.TestoMessaggio);
            this.TipologiaMessaggio = (TextView) itemView.findViewById(R.id.TipologiaMessaggio);
            this.DataMessaggio = (TextView) itemView.findViewById(R.id.DataMessaggio);
            this.textViewIdSegnalazione = (TextView) itemView.findViewById(R.id.textViewIdSegnalazione);
        }
    }

    public AdapterMessaggi(ArrayList<MessaggioCondomino> dataset) {
        this.dataset = dataset;
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
        TextView textViewIdSegnalazione = holder.textViewIdSegnalazione;

        TipologiaMessaggio.setText( dataset.get(listPosition).getData());
        TestoMessaggio.setText( dataset.get(listPosition).getData());
        DataMessaggio.setText(dataset.get(listPosition).getStabile());
        textViewIdSegnalazione.setText(dataset.get(listPosition).getMessaggio().toString());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}