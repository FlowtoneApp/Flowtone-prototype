package site.tenqui.tpncm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import site.tenqui.tpncm.model.Song


@Composable
fun SongListItem(
    song: Song,
    position: GroupPosition,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val backgroundColor =
        if (isPlaying){
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else{
            Color.Transparent
        }
//        Row
//      歌曲信息层
//        Row
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(groupShape(position))
            .background(backgroundColor)
            .clickable{ onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
//        Box
//        封面
//        Box
    Box(
    modifier = Modifier
        .size(56.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small
        ),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
    }

        SongTextBlock(
            title = song.name,
            artist = song.artist,
            isPlaying = isPlaying,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "更多"
            )
        }
    }
}

private fun groupShape(position: GroupPosition): RoundedCornerShape{
val radius = 28.dp
    return when (position) {
        //单个形状为：
        GroupPosition.Single ->
            RoundedCornerShape(radius)

        GroupPosition.Top ->
            RoundedCornerShape(
                topStart = radius,
                topEnd = radius
            )

        GroupPosition.Middle ->
            RoundedCornerShape(0.dp)

        GroupPosition.Bottom ->
            RoundedCornerShape(
                bottomStart = radius,
                bottomEnd = radius
            )
    }
}