package br.eng.pedro_mendes.modulo_iv.data.remote.dto

import java.time.LocalDate

data class StudentRequestDto(
    private val name: String,
    private val surname: String,
    private val birthdate: LocalDate,
)