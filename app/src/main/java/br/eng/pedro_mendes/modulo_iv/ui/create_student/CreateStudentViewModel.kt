package br.eng.pedro_mendes.modulo_iv.ui.create_student

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eng.pedro_mendes.modulo_iv.data.remote.RetrofitClient
import br.eng.pedro_mendes.modulo_iv.data.remote.StudentRepository
import br.eng.pedro_mendes.modulo_iv.data.remote.StudentRepositoryImpl
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentRequestDto
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import br.eng.pedro_mendes.modulo_iv.ui.update_student.UpdateStudentViewModel
import br.eng.pedro_mendes.modulo_iv.utils.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateStudentViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val studentRepository: StudentRepository =
        StudentRepositoryImpl(
            RetrofitClient.getInstance()
        )

    private val _status = MutableStateFlow(Status.Loading)
    val status: Flow<Status> = _status

    fun createStudent(student: StudentRequestDto) {
        viewModelScope.launch(dispatcher) {
            _status.value = Status.Loading

            studentRepository.create(student).enqueue(object : Callback<StudentResponseDto> {
                override fun onResponse(
                    call: Call<StudentResponseDto>,
                    response: Response<StudentResponseDto>,
                ) {
                    if (response.isSuccessful) {
                        _status.value = Status.Success
                    }
                }

                override fun onFailure(call: Call<StudentResponseDto>, t: Throwable) {
                    Log.d(UpdateStudentViewModel::class.java.name, t.stackTraceToString())
                    _status.value = Status.Error
                }

            })
        }
    }

}