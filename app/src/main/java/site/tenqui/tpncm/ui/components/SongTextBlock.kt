package site.tenqui.tpncm.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SongTextBlock(
    title: String,
    artist: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier){
        val playOffset by animateDpAsState(
            targetValue = if (isPlaying) 0.dp else (-24).dp,
            animationSpec = tween(durationMillis = 200),
            label = "PlayOffset"
        )

        val playAlpha by animateFloatAsState(
            targetValue = if (isPlaying) 1f else 0f,
            animationSpec = tween(durationMillis = 100),
            label = "PlayAlpha"
        )

        //歌曲标题
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Playing",
                modifier = Modifier
                    .offset(x = playOffset)
                    .alpha(playAlpha)
                    .size(24.dp)
                    .padding(end = 6.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = title,
                modifier = Modifier.offset(x = playOffset),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = artist,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
    }
}