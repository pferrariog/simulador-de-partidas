package com.simulator.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.simulator.R;
import com.simulator.data.MatchesApi;
import com.simulator.databinding.ActivityMainBinding;
import com.simulator.domain.Match;
import com.simulator.ui.adapter.MatchesAdapter;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MatchesApi matchesApi;
    private MatchesAdapter matchesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHttpClient();
        setupMatchesList();
        setupMatchesRefresh();
        setupFloatingActionButton();

        //essa implementação Java corresponde a mesma implementação feita em Kotlin
    }

    private void setupHttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pferrariog.github.io/api-simulador-de-partidas/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //dessa forma o objeto retrofit está preparado para se criar uma implementação
        //efetiva da interface criada
        //pega-se da base url gerada pelo github pages, indicando a base url da api
        //utiliza do conversor base da google, Gson

        matchesApi = retrofit.create(MatchesApi.class);
        //essa chamada devolve uma instancia efetiva da interface
        //dessa forma o recurso matches.json é chamado corretamente
    }

    private void setupFloatingActionButton() {
        binding.fabSimulate.setOnClickListener(view -> {
            view.animate().rotationBy(360).setDuration(500).setListener(new AnimatorListenerAdapter() {
                //setando uma animação para o botao de simulação de partidas
                //ao clicar ele gira 360 durante 0,5 sec e interagindo com uma animação
                //que tem o algoritmo de simulação implementado nela ao fim da interação com o dado
                @Override
                public void onAnimationEnd(Animator animation) {
                    Random random = new Random();
                    for (int i = 0; i < matchesAdapter.getItemCount(); i++) {
                       Match match = matchesAdapter.getMatches().get(i);
                       match.getHomeTeam().setScore(random.nextInt(match.getHomeTeam().getStars()+1));
                       match.getAwayTeam().setScore(random.nextInt(match.getAwayTeam().getStars()+1));
                       matchesAdapter.notifyItemChanged(i);
                       //a logica se faz presente
                       //o valor do placar é randomizado, porém é definido pela força/estrelas dos times
                       //e ao adicionar o +1, fazemos com que o placar varie de 0 até
                       //o numero maximo de estrelas do time
                       //dessa forma um time com mais estrelas tem uma maior chance de ganhar
                       //pois na teoria é mais forte
                       //e ao terminar, informamos ao matches adapter que um item atualizou
                       //onde ele atualiza e retorna os dados corretos
                    }
                }
            });
        });
    }

    private void setupMatchesRefresh() {
        binding.srlMatches.setOnRefreshListener(this::findMatchesFromApi);
    }

    private void setupMatchesList() {
        //dessa forma implementamos e configuramos as propriedades
        binding.rvMatches.setHasFixedSize(true);
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(this));
        findMatchesFromApi();
        //extrai-se p/ um método pois sabemos que tanto o setup
        //quanto o refresh vao utilizar deste metodo, dessa forma evita repetição de codigo
    }

    private void findMatchesFromApi() {
        binding.srlMatches.setRefreshing(true);
        //setando o botando de refresh p true
        matchesApi.getMatches().enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if (response.isSuccessful()) {
                    List<Match> matches = response.body();
                    //essa chamada cria uma lista das partidas com as chamadas dos dados
                    //se for bem sucedida
                    matchesAdapter = new MatchesAdapter(matches);
                    binding.rvMatches.setAdapter(matchesAdapter);
                } else {
                    showErrorMessage();
                    //caso de erro na chamada do metodo
                }
                binding.srlMatches.setRefreshing(false);
                //setando a finalização dele para qualquer caso, seja de sucesso ou falha
                //pois senao ele ficaria rodando e "atualizando" sem fim
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                showErrorMessage();
                binding.srlMatches.setRefreshing(false);
                //em caso de failure o proprio callback ja aponta um erro
            }
        });
    }

    private void showErrorMessage() {
        Snackbar.make(binding.fabSimulate, R.string.error_api, Snackbar.LENGTH_LONG).show();
        //componente nativo do material, onde passamos uma view de onde ele vai emitir a mensagem de erro
        //dessa vez foi utilizado o floating action button
        //ao inves de passar as mensagens, utilizou-se do conceito de resources para internacionalizar a mensagem
        //o length long indica que a mensagem deve permanecer mais tempo
        //show faz com que a mensagem seja exibida
    }
}
