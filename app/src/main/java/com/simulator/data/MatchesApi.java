package com.simulator.data;

import com.simulator.domain.Match;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MatchesApi {

    @GET("matches.json")
    Call<List<Match>> getMatches();
    //método de get do retrofit
    //acessando o recurso da API, pegando os dados de matches.json (nome do arquivo)

}
