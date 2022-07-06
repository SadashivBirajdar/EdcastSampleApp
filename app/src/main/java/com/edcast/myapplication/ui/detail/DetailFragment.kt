package com.edcast.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.databinding.FragmentDetailBinding
import com.edcast.myapplication.ui.BaseFragment
import com.edcast.myapplication.ui.SampleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sampleViewModel: SampleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        sampleViewModel.getPeopleDetails()
    }

    private fun initObserver() {
        sampleViewModel.liveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                SampleViewModel.Result.Loading -> {
                    showProgress()
                }
                is SampleViewModel.Result.PeopleDetailSuccess -> {
                    hideProgress()
                    updateUI(result.data)
                }
                is SampleViewModel.Result.Failure -> {
                    hideProgress()
                    showMessage(result.error)
                }
            }
        }
    }

    private fun updateUI(people: People) {
        binding.apply {
            txtNameValue.text = people.name
            txtHeightValue.text = people.height
            txtMassValue.text = people.weight
            txtCreatedAtValue.text = people.created
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}