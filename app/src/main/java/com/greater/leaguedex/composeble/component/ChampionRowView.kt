package com.greater.leaguedex.composeble.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.greater.leaguedex.composeble.theme.LeagueDexTheme
import dev.chrisbanes.accompanist.coil.CoilImage

data class ChampionRowModel(
    val id: Long,
    val name: String,
    val image: String,
    val description: String
)

@Composable
fun championRowView(
    model: ChampionRowModel,
    click: () -> Unit
) {
    Box(Modifier.aspectRatio(1f).clickable(onClick = click)) {
        CoilImage(
            data = model.image,
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight()
                .clip(RoundedCornerShape(8.dp)),
            fadeIn = true
        )
        Text(
            text = model.name,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LeagueDexTheme {
        championRowView(
            model = ChampionRowModel(
                id = 0L,
                name = "Khazix",
                image = "http://ddragon.leagueoflegends.com/cdn/10.23.1/img/champion/khazix.png",
                description = "Void Raider"
            )
        ) {}
    }
}
