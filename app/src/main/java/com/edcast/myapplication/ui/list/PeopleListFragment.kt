package com.edcast.myapplication.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.edcast.myapplication.R
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.databinding.FragmentFirstBinding
import com.edcast.myapplication.ui.BaseFragment
import com.edcast.myapplication.ui.SampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PeopleListFragment : BaseFragment(), ListAdapter.PeopleSelectionListener {

    private lateinit var adapter: ListAdapter
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sampleViewModel: SampleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObserver()
        sampleViewModel.getPeoples()
    }

    private fun initUI() {
        adapter = ListAdapter(this)
        binding.recList.layoutManager = LinearLayoutManager(requireContext())
        binding.recList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PeoplesLoadStateAdapter { adapter.retry() },
            footer = PeoplesLoadStateAdapter { adapter.retry() }
        )
        setScrollToTopWHenRefreshedFromNetwork()
    }

    private fun setScrollToTopWHenRefreshedFromNetwork() {
        // Scroll to top when the list is refreshed from network.
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                .collect { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            showProgress()
                        }
                        is LoadState.Error -> {
                            hideProgress()
                            val errorState = loadState.refresh as LoadState.Error
                            showMessage(errorState.error.localizedMessage ?: "Something went wrong")
                        }
                        is LoadState.NotLoading -> {
                            hideProgress()
                            binding.recList.scrollToPosition(0)
                        }
                    }
                }
        }
    }

    private fun initObserver() {
        sampleViewModel.pagingData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPeopleSelected(people: People) {
        sampleViewModel.setSelectedPeople(people)
        val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}