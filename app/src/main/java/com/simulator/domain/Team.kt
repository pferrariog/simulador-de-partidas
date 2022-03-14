package com.simulator.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team(
    //a classe tipo data gera automaticamente os getters e setters, sendo uma vantagem em rel ao java
    //é uma classe de mapeamento e de transferencia de dados
    //encapsulamento automatica e melhora a produtividade, deixa codigo menos verboso


    @SerializedName("nome")
    val name: String,
    @SerializedName("forca")
    val stars: Int,
    @SerializedName("imagem")
    val image: String,

    var score: Int?
    //placar do time, que terá o valor simulado randomicamente
    //ao contrario de val, ao setar uma variavel com var, ela passa a ter get e set
    //não apenas get
) : Parcelable

//esse método mostra a diferença das implementações do parcelable entre kotlin e java