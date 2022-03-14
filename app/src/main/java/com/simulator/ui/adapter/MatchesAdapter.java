package com.simulator.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simulator.databinding.MatchItemBinding;
import com.simulator.domain.Match;
import com.simulator.ui.DetailActivity;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    private List<Match> matches;

    public MatchesAdapter(List<Match> matches) {
        this.matches = matches;
        //como queremos receber a lista, criamos um construtor recebendo as listas como parâmetro
    }

    public List<Match> getMatches() {
        return matches;
        //criando um get da lista para que possamos acessá-la no processo de implementação do algoritmo
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MatchItemBinding binding = MatchItemBinding.inflate(layoutInflater, parent, false);
        //o view holder precisa de um binding, logo recuperamos o layout inflater do contexto
        // do view group para gerar a binding necessaria
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //esse método é onde a partir da instancia do holder e da posiçao
        //conseguimos entender qual partida está ali
        //sendo o local de realmente atribuição de valores
        Context context = holder.itemView.getContext();
        Match match = matches.get(position);

        //adaptando os dados da partida recuperada da API para o layout
        Glide.with(context).load(match.getHomeTeam().getImage()).circleCrop().into(holder.binding.ivHomeTeam);
        holder.binding.tvHomeTeamName.setText(match.getHomeTeam().getName());
        if(match.getHomeTeam().getScore() != null) {
            holder.binding.tvHomeTeamScore.setText(String.valueOf(match.getHomeTeam().getScore()));
            //setamos que apenas quando o score for diferente de nulo que ele deve ser apresentado
            //caso contrario poderia gerar uma null pointer excep
            //forçamos que o score seja uma string pois queremos que o score seja interpretado como um texto
        }
        Glide.with(context).load(match.getAwayTeam().getImage()).circleCrop().into(holder.binding.ivAwayTeam);
        holder.binding.tvAwayTeamName.setText(match.getAwayTeam().getName());
        if(match.getAwayTeam().getScore() != null){
            holder.binding.tvAwayTeamScore.setText(String.valueOf(match.getAwayTeam().getScore()));
        }
        //dessa forma atribuimos os dados da partida aos respectivos views deles

        //implementação da navegação da tela principal para a tela de detalhes
        //utilizando de um intent para trafegar dados através do click
        //seta uma constante que se relaciona com o conteúdo de match
        //dado o contexto prévio no adapter
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.Extras.MATCH, match);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return matches.size();
        //indica o tamanho da lista
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MatchItemBinding binding;
        //dessa forma o elemento de binding pode ser acessado por qualquer elemento dentro da classe


        //necessita-se de um construtor que recebe view e chama essa view "raiz"
        //porem ao utilizar do view binding, podemos ja especificar a view que queremos receber
        //recebendo a raiz/root de fato
        public ViewHolder(MatchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
