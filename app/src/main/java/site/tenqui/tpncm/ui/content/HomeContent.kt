package site.tenqui.tpncm.ui.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.tenqui.tpncm.ui.HomeViewModel
import site.tenqui.tpncm.ui.components.GroupPosition
import site.tenqui.tpncm.ui.components.SongListItem

@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val songs by viewModel.songs
    var currentSongId by remember { mutableStateOf<Long?>(null) }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        itemsIndexed(songs) { index, song ->
            val position = when{
                songs.size == 1 -> GroupPosition.Single
                index == 0 -> GroupPosition.Top
                index == songs.lastIndex -> GroupPosition.Bottom
                else -> GroupPosition.Middle
            }

            SongListItem(
                song = song,
                position = position,
                isPlaying = song.id == currentSongId,
                onClick = {
                    currentSongId = song.id

                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            "正在播放：${song.name} - ${song.artist}",
                            duration = SnackbarDuration.Short
                            )
                    }
                },
                onMoreClick = {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("更多: ${song.name}")
                    }
                }
            )
        }
    }
}
