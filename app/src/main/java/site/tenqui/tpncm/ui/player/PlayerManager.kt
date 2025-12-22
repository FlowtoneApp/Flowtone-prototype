package site.tenqui.tpncm.ui.player

import android.content.Context
import android.net.Uri
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.media3.common.MediaItem
import androidx.media3.common.AudioAttributes
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.common.MediaMetadata
import androidx.media3.ui.PlayerNotificationManager
import site.tenqui.tpncm.R
import site.tenqui.tpncm.model.Song
import android.media.MediaMetadataRetriever
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight

class PlayerManager(private val context: Context) {

    private val player = ExoPlayer.Builder(context).build()
    private var currentSource: String? = null
    private var lastTitle: String? = null
    private var lastArtist: String? = null
    private val mediaSession: MediaSession = MediaSession.Builder(context, player).build()
    private var notificationManager: PlayerNotificationManager? = null
    init {
        val audioAttrs = AudioAttributes.Builder()
            .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(androidx.media3.common.C.USAGE_MEDIA)
            .build()
        player.setAudioAttributes(audioAttrs, true)

        val channelId = "music_playback_channel"
        val channelName = "Music Playback"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(channelId) == null) {
                nm.createNotificationChannel(
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
                )
            }
        }
        notificationManager = PlayerNotificationManager.Builder(context, 1, channelId)
            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: androidx.media3.common.Player): CharSequence {
                    return lastTitle ?: "音频"
                }
                override fun getCurrentContentText(player: androidx.media3.common.Player): CharSequence? {
                    return lastArtist
                }
                override fun getCurrentLargeIcon(
                    player: androidx.media3.common.Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): android.graphics.Bitmap? {
                    return null
                }
                override fun createCurrentContentIntent(player: androidx.media3.common.Player): android.app.PendingIntent? {
                    return null
                }
            })
            .setSmallIconResourceId(site.tenqui.tpncm.R.mipmap.ic_launcher)
            .build()
        notificationManager?.setPlayer(player)
        try {
            notificationManager?.setMediaSessionToken(mediaSession.sessionCompatToken)
        } catch (_: Exception) {}
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
        mediaSession.release()
        notificationManager?.setPlayer(null)
    }

    fun playFile(path: String) {
        val meta = MediaMetadata.Builder()
            .setTitle(lastTitle)
            .setArtist(lastArtist)
            .build()
        val mediaItem = if (path.startsWith("content://")) {
            MediaItem.Builder().setUri(Uri.parse(path)).setMediaMetadata(meta).build()
        } else {
            MediaItem.Builder().setUri(Uri.fromFile(java.io.File(path))).setMediaMetadata(meta).build()
        }
        player.setMediaItem(mediaItem)
        currentSource = path
        player.prepare()
        player.play()
    }

    fun togglePlay(path: String?) {
        if (player.isPlaying) {
            player.pause()
            return
        }
        if (path != null) {
            if (currentSource != path) {
                val meta = MediaMetadata.Builder()
                    .setTitle(lastTitle)
                    .setArtist(lastArtist)
                    .build()
                val mediaItem = if (path.startsWith("content://")) {
                    MediaItem.Builder().setUri(Uri.parse(path)).setMediaMetadata(meta).build()
                } else {
                    MediaItem.Builder().setUri(Uri.fromFile(java.io.File(path))).setMediaMetadata(meta).build()
                }
                player.setMediaItem(mediaItem)
                currentSource = path
                player.prepare()
            }
        }
        player.play()
    }

    fun setNowPlayingInfo(title: String?, artist: String?) {
        lastTitle = title
        lastArtist = artist
    }

    fun isPlaying(): Boolean = player.isPlaying
}

@Composable
fun MiniPlayer(
    song: Song?,
    isPlaying: Boolean,
    onToggle: () -> Unit,
    onOpenPlayer: () -> Unit,
    onInfoPositioned: (Offset) -> Unit,
    showText: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onOpenPlayer) {
            Box(modifier = Modifier.onGloballyPositioned { 
                if (showText) onInfoPositioned(it.positionInRoot()) 
            }) {
                if (showText) {
                    Text(if (song != null) "${song.name} - ${song.artist}" else "未选择歌曲")
                } else {
                    // Empty capsule text
                    Text("")
                }
            }
        }
        Button(onClick = onToggle) { Text(if (isPlaying) "暂停" else "播放") }
    }
}

@Composable
fun PlayerScreen(
    song: Song?,
    isPlaying: Boolean,
    onToggle: () -> Unit,
    onClose: () -> Unit,
    padding: androidx.compose.foundation.layout.PaddingValues,
    showInfo: Boolean,
    onInfoPositioned: (Offset) -> Unit
) {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val cover = remember(song?.path) {
        try {
            if (song?.path != null) {
                val mmr = MediaMetadataRetriever()
                if (song.path.startsWith("content://")) {
                    mmr.setDataSource(context, Uri.parse(song.path))
                } else {
                    mmr.setDataSource(song.path)
                }
                val bytes = mmr.embeddedPicture
                mmr.release()
                if (bytes != null) android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap() else null
            } else null
        } catch (e: Exception) { null }
    }
    val topPadding = padding.calculateTopPadding()
    val startPadding = padding.calculateLeftPadding(layoutDirection)
    val endPadding = padding.calculateRightPadding(layoutDirection)
    
    val artistStyle = MaterialTheme.typography.bodyMedium
    val titleStyle = artistStyle.copy(
        fontSize = artistStyle.fontSize * 1.5f,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = startPadding, top = topPadding, end = endPadding, bottom = 0.dp)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cover != null) {
                Image(
                    bitmap = cover,
                    contentDescription = null,
                    modifier = Modifier.size(240.dp).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onToggle) { Text(if (isPlaying) "暂停" else "播放") }
            }
        }
        
        if (showInfo) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.dp, bottom = 24.dp)
                    .onGloballyPositioned { onInfoPositioned(it.positionInRoot()) },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = song?.name ?: "",
                    style = titleStyle
                )
                Text(
                    text = song?.artist ?: "",
                    style = artistStyle
                )
            }
        }
        
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 24.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
        }
    }
}
