package br.eng.pedro_mendes.modulo_iv.data.remote

import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentRequestDto
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface StudentRepository {
    @GET("/alunos")
    fun get(): Call<List<StudentResponseDto>>

    @GET("/alunos/{id}")
    fun find(@Path("id") id: String): Call<StudentResponseDto>

    @POST("/alunos")
    fun create(@Body student: StudentRequestDto): Call<StudentResponseDto>

    @PUT("/alunos/{id}")
    fun update(@Path("id") id: String, @Body student: StudentRequestDto): Call<StudentResponseDto>

    @DELETE("/alunos/{id}")
    fun delete(@Path("id") id: String): Call<ResponseBody>
}