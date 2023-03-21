package br.eng.pedro_mendes.modulo_iv.ui.update_student

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
import androidx.navigation.fragment.navArgs
import br.eng.pedro_mendes.modulo_iv.R
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentRequestDto
import br.eng.pedro_mendes.modulo_iv.databinding.FragmentUpdateStudentBinding
import br.eng.pedro_mendes.modulo_iv.utils.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.LocalDate


class UpdateStudentFragment : Fragment() {

    private var _binding: FragmentUpdateStudentBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UpdateStudentViewModel by viewModels()
    private val args: UpdateStudentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentUpdateStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
        setupObservers()
    }

    private fun setupView() {
        binding.apply {
            args.student.let {
                editTextName.setText(it.name)
                editTextSurname.setText(it.surname)
                editTextBirthDate.setText(it.birthdate.toString())
            }
        }
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

            buttonUpdate.setOnClickListener {
                viewModel.updateStudent(
                    args.student.id,
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