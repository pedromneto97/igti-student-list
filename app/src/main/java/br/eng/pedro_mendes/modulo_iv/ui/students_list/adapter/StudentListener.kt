package br.eng.pedro_mendes.modulo_iv.ui.students_list.adapter

import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto

interface StudentListener {
    fun onClickStudent(id: String)

    fun onClickEditStudent(student: StudentResponseDto)

    fun onClickDeleteStudent(id: String)
}