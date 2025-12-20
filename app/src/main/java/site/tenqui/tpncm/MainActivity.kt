package site.tenqui.tpncm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import site.tenqui.tpncm.ui.HomeScreen
import site.tenqui.tpncm.ui.HomeViewModel
import site.tenqui.tpncm.ui.player.MiniPlayer
import site.tenqui.tpncm.ui.player.PlayerManager
import site.tenqui.tpncm.ui.theme.MyMusicApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyMusicApplicationTheme {
                val context = LocalContext.current
                val playerManager = remember { PlayerManager(context) }
                val viewModel: HomeViewModel = viewModel()

                Column {
                    HomeScreen(viewModel)

                    MiniPlayer(
                        onPlay = { playerManager.play() },
                        onPause = { playerManager.pause() }
                    )
                }
            }
        }
    }
}
