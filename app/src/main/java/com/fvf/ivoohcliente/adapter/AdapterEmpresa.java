package com.fvf.ivoohcliente.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fvf.ivoohcliente.R;
import com.fvf.ivoohcliente.model.Estabelecimento;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder> {

    private List<Estabelecimento> estabelecimentos;

    public AdapterEmpresa(List<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_empresa, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Estabelecimento estabelecimento = estabelecimentos.get(i);
        holder.nomeEmpresa.setText(estabelecimento.getNome());
        holder.categoria.setText(estabelecimento.getCategoria() + " - ");
        holder.tempo.setText(estabelecimento.getTempo() + " Min");
        holder.entrega.setText("R$ " + estabelecimento.getPrecoEntrega().toString());

        //Carregar imagem
        String urlImagem = estabelecimento.getUrlImagem();
        Picasso.get().load( urlImagem ).into(holder.imagemEmpresa);

    }

    @Override
    public int getItemCount() {
        return estabelecimentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemEmpresa;
        TextView nomeEmpresa;
        TextView categoria;
        TextView tempo;
        TextView entrega;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textNomeEmpresa);
            categoria = itemView.findViewById(R.id.textCategoriaEmpresa);
            tempo = itemView.findViewById(R.id.textTempoEmpresa);
            entrega = itemView.findViewById(R.id.textEntregaEmpresa);
            imagemEmpresa = itemView.findViewById(R.id.imageEmpresa);
        }
    }
}
