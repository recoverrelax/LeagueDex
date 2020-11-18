package com.greater.leaguedex.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.ActMainBinding
import com.greater.leaguedex.mvvm.BaseActivity
import com.greater.leaguedex.ui.championlist.ChampionListFrag
import com.greater.leaguedex.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAct : BaseActivity<MainViewModel, MainViewStates>() {
    override val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ActMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            replaceFragment(
                containerId = R.id.container,
                fragmentSupplier = {
                    ChampionListFrag.newInstance()
                },
                addToBackStack = false
            )
        }
    }

    override fun render(viewState: MainViewStates) {}
}
