package br.eng.pedro_mendes.modulo_iv.ui.student_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eng.pedro_mendes.modulo_iv.data.remote.RetrofitClient
import br.eng.pedro_mendes.modulo_iv.data.remote.StudentRepository
import br.eng.pedro_mendes.modulo_iv.data.remote.StudentRepositoryImpl
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import br.eng.pedro_mendes.modulo_iv.utils.Result
import br.eng.pedro_mendes.modulo_iv.view_model.StudentViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentDetailsViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) :
    ViewModel() {
    private val studentRepository: StudentRepository =
        StudentRepositoryImpl(
            RetrofitClient.getInstance()
        )

    private val _status = MutableStateFlow<Result<StudentResponseDto>>(Result.loading(null))
    val status: Flow<Result<StudentResponseDto>> = _status

    fun findStudent(id: String) {
        viewModelScope.launch(dispatcher) {
            _status.value = Result.loading(null)

            studentRepository.find(id).enqueue(object : Callback<StudentResponseDto> {
                override fun onResponse(
                    call: Call<StudentResponseDto>,
                    response: Response<StudentResponseDto>,
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _status.value = Result.success(it)
                        }
                    }
                }

                override fun onFailure(call: Call<StudentResponseDto>, t: Throwable) {
                    Log.e(StudentViewModel::class.java.name, t.toString())
                    _status.value = Result.error(t.message.toString(), null)
                }

            })
        }
    }

}