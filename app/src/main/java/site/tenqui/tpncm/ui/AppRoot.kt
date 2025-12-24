package site.tenqui.tpncm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import site.tenqui.tpncm.ui.player.PlayerBar
import site.tenqui.tpncm.ui.player.PlayerManager
import site.tenqui.tpncm.ui.player.PlayerState

@Composable
fun AppRoot(){
    val viewModel: HomeViewModel = viewModel()
    val playerManager = PlayerManager()

    Box(modifier = Modifier.fillMaxSize()){

        HomeScreen(viewModel = viewModel)

        if (PlayerState.hasTrack.value){
            PlayerBar(
                isPlaying = PlayerState.isPlaying.value,
                onPrev = { playerManager.previous() },
                onPlayPause = { playerManager.toggle() },
                onNext = { playerManager.next() },
                modifier = Modifier
                    .align (Alignment.BottomCenter)
                    .padding(bottom = 88.dp)
            )
        }
    }
}