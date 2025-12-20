package site.tenqui.tpncm.ui.player

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import site.tenqui.tpncm.R

class PlayerManager(private val context: Context) {

    private val player = ExoPlayer.Builder(context).build()

    fun play() {
        val mediaItem = MediaItem.fromUri(
            "android.resource://${context.packageName}/${R.raw.test01}"
        )
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
    }
}

@Composable
fun MiniPlayer(
    onPlay: () -> Unit,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { /* prev */ }) {
            Text("⏮")
        }
        Button(onClick = onPlay) {
            Text("▶")
        }
        Button(onClick = onPause) {
            Text("⏸")
        }
        Button(onClick = { /* next */ }) {
            Text("⏭")
        }
    }
}

