package com.greater.leaguedex.ui.championlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.FragChampionlistBinding
import com.greater.leaguedex.mvvm.BaseFragment
import com.greater.leaguedex.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChampionListFrag :
    BaseFragment<ChampionLisViewModel, ChampionListState>(R.layout.frag_championlist) {
    override val viewModel: ChampionLisViewModel by viewModels()
    private val binding by viewBinding(FragChampionlistBinding::bind)

    private val myAdapter = ChampionListAdapter()
    private val myLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(this.context, 3)
    }

    companion object {
        fun newInstance(): ChampionListFrag {
            return ChampionListFrag()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.itemList) {
            layoutManager = myLayoutManager
            adapter = myAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun render(viewState: ChampionListState) {
        when (viewState) {
            is ChampionListState.UpdateItemList -> myAdapter.setAdapterItems(viewState.itemList)
        }
    }
}
