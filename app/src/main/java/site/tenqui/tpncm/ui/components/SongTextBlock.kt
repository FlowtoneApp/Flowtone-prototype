package site.tenqui.tpncm.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SongTextBlock(
    title: String,
    subtitle: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier){
        //歌曲标题
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            if (isPlaying){
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Playing",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
    }
}