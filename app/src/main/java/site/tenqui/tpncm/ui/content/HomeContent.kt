package site.tenqui.tpncm.ui.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import site.tenqui.tpncm.ui.components.SongListItem
import site.tenqui.tpncm.ui.player.PlayerManager

@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    playerManager: PlayerManager
) {
    val songs by viewModel.songs
    var currentSongId by remember { mutableStateOf<Long?>(null) }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        items(songs){ song ->
            SongListItem(
                song = song,
                isPlaying = song.id == currentSongId,
                onClick = {
                    currentSongId = song.id
                    viewModel.setCurrentSong(song)
                    playerManager.setNowPlayingInfo(song.name, song.artist)
                    if (song.path != null) {
                        playerManager.playFile(song.path)
                        viewModel.setPlaying(true)
                    }

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
