package com.example.UrbanTrip.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.UrbanTrip.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String utenti[],descrizioneTestuale[];
    double numero_stelle[];
    Context context;

    public MyAdapter(Context ct, String ut[], String descr_t[], double stelle[]){
        context = ct;
        utenti=ut;
        descrizioneTestuale=descr_t;
        numero_stelle=stelle;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textStelle.setText(Double.toString(numero_stelle[position]));
        holder.textUtente.setText(utenti[position]);
        holder.textDescrizioneTestuale.setText(descrizioneTestuale[position]);
    }

    @Override
    public int getItemCount() {
        return numero_stelle.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textStelle,textUtente,textDescrizioneTestuale;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textStelle = itemView.findViewById(R.id.textViewNumeroStelleRecensione);
            textUtente = itemView.findViewById(R.id.textViewUtenteRecensione);
            textDescrizioneTestuale = itemView.findViewById(R.id.textViewDescrizioneTestualeRecensione);

        }
    }
}
