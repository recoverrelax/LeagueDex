package com.greater.leaguedex.ui.main.people

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        with(binding.peopleList) {
            layoutManager = myLayoutManager
            adapter = myAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val pos = getFirstVisibleKey()
        if (pos != null) {
            val item = myAdapter.snapshot().getOrNull(pos)?.name
            if(item != null){
                viewModel.saveState(item)
            }
        }
        super.onSaveInstanceState(outState)
    }

    private fun getFirstVisibleKey(): Int? {
        val pos = myLayoutManager.findFirstCompletelyVisibleItemPosition()
        return if (pos == RecyclerView.NO_POSITION) null else pos
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun render(viewState: PeopleViewStates) {
        when (viewState) {
            is PeopleViewStates.UpdateList -> {
                lifecycleScope.launch {
                    myAdapter.submitData(viewState.data)
                }
            }
        }
    }
}