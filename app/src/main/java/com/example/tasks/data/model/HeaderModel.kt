package com.example.tasks.data.model

import com.google.gson.annotations.SerializedName
import dagger.Provides
import java.io.Serializable

/**
 * Esse model é referente aos dados que vem da API
 * quando o usuário efetua login ou cria uma conta.
 * Para fazer requisições já dentro do aplicativo
 * é necessário utilizar os headers, que neste caso,
 * são justamente esses dados retornados, por isso
 * foi posto o nome da dataClass de HeaderModel ao
 * invés de AuthModel.
 */

data class HeaderModel(

    @SerializedName("token")
    val token: String,

    @SerializedName("personKey")
    val personKey: String,

    @SerializedName("name")
    val name: String

) : Serializable
