package site.tenqui.tpncm.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val songs by viewModel.songs

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        items(songs){ song ->
            Text(
                text = "${song.name} - ${song.artist}",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable{
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "正在播放: ${song.name} - ${song.artist}"
                            )
                        }
                    }
            )
        }
    }
}
