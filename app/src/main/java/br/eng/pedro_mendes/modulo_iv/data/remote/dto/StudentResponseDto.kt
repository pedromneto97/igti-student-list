package br.eng.pedro_mendes.modulo_iv.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class StudentResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("nome") val name: String,
    @SerializedName("sobrenome") val surname: String,
    @SerializedName("nascimento") val birthdate: LocalDate,
) : Serializable