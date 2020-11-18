package com.greater.leaguedex.ui.championlist

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.viewModels
import androidx.ui.tooling.preview.Preview
import com.greater.leaguedex.composeble.component.ChampionRowModel
import com.greater.leaguedex.composeble.component.championRowView
import com.greater.leaguedex.composeble.theme.LeagueDexTheme
import com.greater.leaguedex.mvvm.BaseFragment
import com.greater.leaguedex.util.LazyGridFor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChampionListFrag :
    BaseFragment<ChampionLisViewModel, ChampionListState>() {
    override val viewModel: ChampionLisViewModel by viewModels()

    companion object {
        fun newInstance(): ChampionListFrag {
            return ChampionListFrag()
        }
    }

    private val itemList: MutableState<List<ChampionRowModel>> = mutableStateOf(emptyList())

    override val content: @Composable () -> Unit = {
        LeagueDexTheme {
            Surface(color = Color.Black) {
                LazyGridFor(
                    items = itemList.value,
                    rows = 2,
                    itemPadding = 0
                ) { item, pos ->
                    championRowView(
                        model = item,
                        click = {
                            onItemClicked(item, pos)
                        }
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        content()
    }

    private fun onItemClicked(model: ChampionRowModel, pos: Int) {
        Timber.i("Clicked item: $model with pos: $pos")
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun render(viewState: ChampionListState) {
        when (viewState) {
            is ChampionListState.UpdateItemList -> {
                itemList.value = viewState.itemList
            }
        }
    }
}
