package br.eng.pedro_mendes.modulo_iv.ui.student_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import br.eng.pedro_mendes.modulo_iv.R
import br.eng.pedro_mendes.modulo_iv.data.remote.dto.StudentResponseDto
import br.eng.pedro_mendes.modulo_iv.databinding.FragmentStudentDetailsBinding
import br.eng.pedro_mendes.modulo_iv.utils.Status
import kotlinx.coroutines.launch

class StudentDetailsFragment : Fragment() {

    private var _binding: FragmentStudentDetailsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: StudentDetailsViewModel by viewModels()
    private val args: StudentDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservables()
    }

    private fun setupObservables() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.findStudent(args.id)
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.status.collect {
                    when (it.status) {
                        Status.Loading -> showLoading()
                        Status.Success -> showDetails(it.data!!)
                        Status.Error -> showError()
                    }
                }
            }
        }
    }

    private fun showError() {
        binding.progressIndicatorFindStudent.visibility = View.GONE

        Toast.makeText(
            requireContext(),
            getString(R.string.failed_to_find_studant),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showDetails(data: StudentResponseDto) {
        binding.apply {
            progressIndicatorFindStudent.visibility = View.GONE
            textViewId.text = data.id
            textViewStudentName.text = data.name
            textViewStudentSurname.text = data.surname
            textViewStudentBirthDate.text = data.birthdate.toString()
        }

    }

    private fun showLoading() {
        binding.progressIndicatorFindStudent.visibility = View.VISIBLE
    }

}