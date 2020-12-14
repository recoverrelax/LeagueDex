package com.greater.leaguedex.ui.main.people

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.FragPeopleBinding
import com.greater.leaguedex.mvvm.BaseFragment
import com.greater.leaguedex.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PeopleFrag : BaseFragment<PeopleViewModel, PeopleViewStates>(R.layout.frag_people) {
    override val viewModel: PeopleViewModel by viewModels()
    private val binding by viewBinding(FragPeopleBinding::bind)

    companion object {
        fun newInstance(): PeopleFrag {
            return PeopleFrag()
        }
    }

    private val myLayoutManager: LinearLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
    }
    private val myAdapter: PeopleAdapter = PeopleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setOnRefreshListener { viewModel.onRefreshRequested() }

        with(binding.peopleList) {
            layoutManager = myLayoutManager
            adapter = myAdapter
        }
        viewModel.initialize()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val pos = getFirstVisibleKey()
        if (pos != null) {
            val item = myAdapter.snapshot().getOrNull(pos)?.name
            if (item != null) {
                viewModel.saveState(item)
            }
        }
        super.onSaveInstanceState(outState)
    }

    private fun getFirstVisibleKey(): Int? {
        val pos = myLayoutManager.findFirstCompletelyVisibleItemPosition()
        return if (pos == RecyclerView.NO_POSITION) null else pos
    }

    override fun render(viewState: PeopleViewStates) {
        when (viewState) {
            is PeopleViewStates.UpdateList -> {
                lifeCycleScope.launch {
                    myAdapter.submitData(viewState.data)
                }
            }
            is PeopleViewStates.RequestSwipeRefresh -> {
                val (isRefreshing, refreshAdapter) = viewState
                binding.swipeRefresh.isRefreshing = isRefreshing
                if(refreshAdapter) myAdapter.refresh()
            }
            PeopleViewStates.ShowSyncError -> {
                binding.swipeRefresh.isRefreshing = false
                Snackbar.make(binding.root, "Sync Failed. Try again", LENGTH_LONG)
                    .show()
            }
        }
    }
}
