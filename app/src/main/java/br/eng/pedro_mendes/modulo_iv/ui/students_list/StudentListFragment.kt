package br.eng.pedro_mendes.modulo_iv.ui.students_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.eng.pedro_mendes.modulo_iv.R
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import br.eng.pedro_mendes.modulo_iv.databinding.FragmentListStudentsBinding
import br.eng.pedro_mendes.modulo_iv.ui.students_list.adapter.StudentAdapter
import br.eng.pedro_mendes.modulo_iv.ui.students_list.adapter.StudentListener
import br.eng.pedro_mendes.modulo_iv.utils.Status
import br.eng.pedro_mendes.modulo_iv.view_model.StudentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StudentListFragment : Fragment() {

    private var _binding: FragmentListStudentsBinding? = null

    private val binding get() = _binding!!
    private val viewModel: StudentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentListStudentsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        setupListeners()
    }

    private fun setupListeners() {
        binding.fabAddStudent.setOnClickListener {
            findNavController().navigate(StudentListFragmentDirections.actionStudentsListFragmentToCreateStudentFragment())
        }
    }

    private fun setupViewModel() {
        getStudentsOnCreated()
        collectStudentsListOnResumed()
        collectDeleteStatus()
    }

    private fun collectDeleteStatus() {
        lifecycleScope.launch {
            viewModel.deleteStudentStatus.collect {
                if (it == Status.Success) {
                    viewModel.getStudents()
                    Snackbar.make(
                        requireView(),
                        getString(R.string.student_deleted_successfully),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (it == Status.Error) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.failed_to_delete_student),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        }
    }

    private fun collectStudentsListOnResumed() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.studentsList.collect {
                    if (it.isNotEmpty()) {
                        binding.recyclerViewStudentList.adapter = StudentAdapter(
                            studentList = it,
                            studentListener = object : StudentListener {
                                override fun onClickStudent(id: String) {
                                    findNavController().navigate(
                                        StudentListFragmentDirections.actionStudentsListFragmentToStudentDetailsFragment(
                                            id
                                        )
                                    )

                                }

                                override fun onClickEditStudent(student: StudentResponseDto) {
                                    findNavController().navigate(
                                        StudentListFragmentDirections.actionStudentsListFragmentToUpdateStudentFragment2(
                                            student
                                        )
                                    )
                                }

                                override fun onClickDeleteStudent(id: String) =
                                    viewModel.deleteStudent(id)

                            }
                        )
                    }
                }
            }
        }
    }

    private fun getStudentsOnCreated() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getStudents()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewStudentList.apply {
            val context = requireView().context

            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}