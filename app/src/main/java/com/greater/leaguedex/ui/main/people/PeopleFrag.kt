package com.greater.leaguedex.ui.main.people

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.FragPeopleBinding
import com.greater.leaguedex.mvvm.BaseFragment
import com.greater.leaguedex.ui.main.actions.SnackBarManager
import com.greater.leaguedex.util.activityAction
import com.greater.leaguedex.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

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
    private val myAdapter: PeopleAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PeopleAdapter(
            onFavouriteClicked = viewModel::onFavouriteClicked
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.refresh -> {
                    viewModel.onRefreshRequested()
                    true
                }
                else -> false
            }
        }

        with(binding.peopleList) {
            layoutManager = myLayoutManager
            adapter = myAdapter
            setHasFixedSize(true)
        }
        viewModel.initialize()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val pos = getFirstVisibleKey()
        if (pos != null) {
            val item = myAdapter.currentList?.getOrNull(pos)?.name
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

    private fun dispatchLoadingStates(viewState: PeopleViewStates.LoadingStatus) {
        binding.peopleList.visibility = if (!viewState.isLoading) View.VISIBLE else View.INVISIBLE
        binding.loadingAnimation.visibility =
            if (viewState.isLoading) View.VISIBLE else View.INVISIBLE
        binding.toolbar.menu.findItem(R.id.refresh).isVisible = !viewState.isLoading

        if (viewState.syncError) {
            activityAction<SnackBarManager> {
                showSnackBar("Sync Failed. Try again")
            }
        }
    }

    override fun render(viewState: PeopleViewStates) {
        when (viewState) {
            is PeopleViewStates.UpdateList -> myAdapter.submitList(viewState.data)
            is PeopleViewStates.LoadingStatus -> dispatchLoadingStates(viewState)
        }
    }
}
