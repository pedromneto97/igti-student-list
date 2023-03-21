package br.eng.pedro_mendes.modulo_iv.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eng.pedro_mendes.modulo_iv.data.remote.RetrofitClient
import br.eng.pedro_mendes.modulo_iv.data.remote.StudentRepository
import br.eng.pedro_mendes.modulo_iv.data.remote.StudentRepositoryImpl
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import br.eng.pedro_mendes.modulo_iv.utils.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val studentRepository: StudentRepository =
        StudentRepositoryImpl(
            RetrofitClient.getInstance()
        )

    private val _studentsList: MutableStateFlow<List<StudentResponseDto>> =
        MutableStateFlow(listOf())
    val studentsList: Flow<List<StudentResponseDto>> = _studentsList

    private val _deleteStudentStatus: MutableStateFlow<Status> = MutableStateFlow(Status.Loading)
    val deleteStudentStatus: Flow<Status> = _deleteStudentStatus

    fun getStudents() {
        viewModelScope.launch(dispatcher) {
            studentRepository.get().enqueue(
                object : Callback<List<StudentResponseDto>> {
                    override fun onResponse(
                        call: Call<List<StudentResponseDto>>,
                        response: Response<List<StudentResponseDto>>,
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                _studentsList.value = it
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<StudentResponseDto>>, t: Throwable) {
                        Log.e(StudentViewModel::class.java.name, t.toString())
                        _studentsList.value = listOf()
                    }

                }
            )

        }

    }

    fun deleteStudent(id: String) {
        viewModelScope.launch(dispatcher) {
            _deleteStudentStatus.value = Status.Loading

            studentRepository.delete(id).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    _deleteStudentStatus.value =
                        if (response.isSuccessful) Status.Success else Status.Error
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(StudentViewModel::class.java.name, t.message.toString())
                    _deleteStudentStatus.value = Status.Error
                }
            })
        }
    }
}