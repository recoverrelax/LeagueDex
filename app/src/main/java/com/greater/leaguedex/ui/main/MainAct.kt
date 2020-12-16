package com.greater.leaguedex.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.ActMainBinding
import com.greater.leaguedex.mvvm.BaseActivity
import com.greater.leaguedex.ui.main.people.PeopleFrag
import com.greater.leaguedex.ui.main.settings.SettingsFrag
import com.greater.leaguedex.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAct : BaseActivity<MainViewModel, MainViewStates>() {
    enum class Screen {
        People,
        Settings
    }

    override val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ActMainBinding::inflate)
    private val navMediator = BottomNavigationViewMediator(
        fragmentManager = supportFragmentManager,
        screenMenuIdMapper = listOf(
            Screen.People to R.id.people,
            Screen.Settings to R.id.about
        ),
        instantiateFrag = { screen ->
            when (screen) {
                Screen.People -> PeopleFrag.newInstance()
                Screen.Settings -> SettingsFrag.newInstance()
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navMediator.initialize(
            activity = this,
            navView = binding.bottomNavView,
            initialScreen = Screen.People
        )
    }

    override fun render(viewState: MainViewStates) {}
}
