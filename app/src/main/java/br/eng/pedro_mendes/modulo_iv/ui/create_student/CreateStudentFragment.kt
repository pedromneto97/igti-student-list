package br.eng.pedro_mendes.modulo_iv.ui.create_student

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
import br.eng.pedro_mendes.modulo_iv.R
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentRequestDto
import br.eng.pedro_mendes.modulo_iv.databinding.FragmentCreateStudentBinding
import br.eng.pedro_mendes.modulo_iv.utils.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.LocalDate


class CreateStudentFragment : Fragment() {

    private var _binding: FragmentCreateStudentBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CreateStudentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentCreateStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.status.collect {
                    if (it == Status.Loading) {
                        return@collect
                    }

                    val text =
                        if (it == Status.Success) {
                            goBack()
                            R.string.create_successful
                        } else R.string.failed_to_create

                    Snackbar.make(
                        requireView(),
                        text,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun goBack() =
        findNavController().popBackStack()

    private fun setupListeners() {
        binding.apply {
            buttonBack.setOnClickListener {
                goBack()
            }

            buttonCreate.setOnClickListener {
                viewModel.createStudent(
                    StudentRequestDto(
                        name = editTextName.text.toString(),
                        surname = editTextSurname.text.toString(),
                        birthdate = LocalDate.parse(editTextBirthDate.text.toString())
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}