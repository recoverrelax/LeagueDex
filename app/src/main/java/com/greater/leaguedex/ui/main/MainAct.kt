package com.greater.leaguedex.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.ActMainBinding
import com.greater.leaguedex.mvvm.BaseActivity
import com.greater.leaguedex.ui.main.people.PeopleFrag
import com.greater.leaguedex.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAct : BaseActivity<MainViewModel, MainViewStates>() {
    override val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ActMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            supportFragmentManager.commit{
                setReorderingAllowed(true)
                add(R.id.container, PeopleFrag.newInstance(), PeopleFrag::class.java.simpleName)
            }
        }
    }

    override fun render(viewState: MainViewStates) {}
}
