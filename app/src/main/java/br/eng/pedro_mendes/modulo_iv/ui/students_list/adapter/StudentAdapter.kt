package br.eng.pedro_mendes.modulo_iv.ui.students_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import br.eng.pedro_mendes.modulo_iv.databinding.StudentItemRowBinding


class StudentAdapter(
    private val studentList: List<StudentResponseDto>,
    private val studentListener: StudentListener,
) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(val binding: StudentItemRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudentItemRowBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val studentResponseDto = studentList[position]

        with(viewHolder) {
            binding.apply {
                textViewStudentName.text = studentResponseDto.name
                textViewStudentSurname.text = studentResponseDto.surname
                root.setOnClickListener {
                    studentListener.onClickStudent(studentResponseDto.id)
                }
                buttonEdit.setOnClickListener {
                    studentListener.onClickEditStudent(studentResponseDto)
                }
                buttonDelete.setOnClickListener {
                    studentListener.onClickDeleteStudent(studentResponseDto.id)
                }
            }
        }
    }

    override fun getItemCount() = studentList.size

}
