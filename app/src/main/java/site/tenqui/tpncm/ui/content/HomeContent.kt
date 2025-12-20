package site.tenqui.tpncm.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.tenqui.tpncm.ui.HomeViewModel

@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val songs by viewModel.songs

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        songs.forEach { song ->
            Text(
                text = "${song.name} - ${song.artist}",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "正在播放：${song.name} - ${song.artist}"
                            )
                        }
                    }
            )
        }
    }
}
