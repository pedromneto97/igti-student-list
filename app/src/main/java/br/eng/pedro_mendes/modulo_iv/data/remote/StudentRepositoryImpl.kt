package br.eng.pedro_mendes.modulo_iv.data.remote

import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentRequestDto
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import okhttp3.ResponseBody
import retrofit2.Call

class StudentRepositoryImpl(private val retrofitClient: RetrofitClient) : StudentRepository {
    override fun get(): Call<List<StudentResponseDto>> = retrofitClient.studentRepository.get()

    override fun find(id: String): Call<StudentResponseDto> =
        retrofitClient.studentRepository.find(id)

    override fun create(student: StudentRequestDto): Call<StudentResponseDto> =
        retrofitClient.studentRepository.create(student)

    override fun update(id: String, student: StudentRequestDto): Call<StudentResponseDto> =
        retrofitClient.studentRepository.update(id, student)

    override fun delete(id: String): Call<ResponseBody> =
        retrofitClient.studentRepository.delete(id)

}