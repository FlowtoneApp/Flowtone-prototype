package site.tenqui.tpncm.ui.player

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp

@Composable
fun PlayerDock(
    onCoverClick: () -> Unit,
    onPrev: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Dock 收起时的露出高度
    val peekHeight = 56.dp

    // 运行时测量 Dock 自己的高度
    var dockHeightPx by remember { mutableStateOf(0) }
    val density = androidx.compose.ui.platform.LocalDensity.current
    val dockHeightDp = with(density) { dockHeightPx.toDp() }

    // 收起时下移的高度：总高度 - 露出的高度
    val hiddenOffset = (dockHeightDp - peekHeight).coerceAtLeast(0.dp)

    val targetOffset = if (expanded) 0.dp else hiddenOffset

    val animatedOffset by animateDpAsState(
        targetValue = targetOffset,
        animationSpec = tween(
            durationMillis = 200,     // 动画时长
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "dockOffset"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                dockHeightPx = coordinates.size.height
            }
            .offset(y = animatedOffset)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -20) expanded = true
                    if (dragAmount > 20) expanded = false
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        // 小白条内容
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount < -20) expanded = true
                        if (dragAmount > 20) expanded = false
                    }
                },
            contentAlignment = Alignment.CenterEnd
        ) {

            Box(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        androidx.compose.material3.MaterialTheme
                            .colorScheme.onSurface.copy(alpha = 0.35f)
                    )
            )
        }

        // ===== 封面 =====
        Box(
            modifier = Modifier
                .size(96.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
                .clickable { onCoverClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.MusicNote, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ===== 控件托盘 =====
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 3.dp,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onPrev) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "上一曲")
                }
                IconButton(onClick = onPlayPause) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "播放/暂停")
                }
                IconButton(onClick = onNext) {
                    Icon(Icons.Default.SkipNext, contentDescription = "下一曲")
                }
            }
        }
    }
}