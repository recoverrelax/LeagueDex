package com.greater.leaguedex.ui.main.settings

import androidx.fragment.app.viewModels
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.FragSettingsBinding
import com.greater.leaguedex.mvvm.BaseFragment
import com.greater.leaguedex.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFrag : BaseFragment<SettingsViewModel, SettingsViewStates>(R.layout.frag_settings) {
    override val viewModel: SettingsViewModel by viewModels()
    private val binding by viewBinding(FragSettingsBinding::bind)

    companion object {
        fun newInstance(): SettingsFrag {
            return SettingsFrag()
        }
    }

    override fun render(viewState: SettingsViewStates) {
        TODO("Not yet implemented")
    }
}
