package site.tenqui.tpncm.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlaybackOverlay(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 72.dp)
    ) {
        Surface(shape = RoundedCornerShape(28.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.align(Alignment.Center)
            ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

//              ⏮⏮⏮⏮
//              上一首
//              ⏮⏮⏮⏮
                FilledIconButton(
                    onClick = {},
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp
                    )
                ){
                    Icon(Icons.Default.SkipPrevious, null)
                }

//              ⏯⏯⏯⏯⏯⏯⏯
//              播放/暂停
//              ⏯⏯⏯⏯⏯⏯⏯
                FilledIconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Default.PlayArrow, null)
                }

//              ⏭⏭⏭⏭
//              下一首
//              ⏭⏭⏭⏭
                FilledTonalButton(
                    onClick = {},
                    shape = RoundedCornerShape(
                        topEnd = 20.dp,
                        bottomEnd = 20.dp
                    )
                ) {
                    Icon(Icons.Default.SkipNext, null)
                }
            }
        }
    }
}