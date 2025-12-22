package site.tenqui.tpncm.ui.player

import android.content.Context
import android.net.Uri
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
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
import androidx.media3.common.AudioAttributes
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.common.MediaMetadata
import androidx.media3.ui.PlayerNotificationManager
import site.tenqui.tpncm.R
import site.tenqui.tpncm.model.Song

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
    onOpenPlayer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onOpenPlayer) { Text(if (song != null) "${song.name} - ${song.artist}" else "未选择歌曲") }
        Button(onClick = onToggle) { Text(if (isPlaying) "暂停" else "播放") }
    }
}

@Composable
fun PlayerScreen(
    song: Song?,
    isPlaying: Boolean,
    onToggle: () -> Unit,
    onClose: () -> Unit,
    padding: androidx.compose.foundation.layout.PaddingValues
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .padding(24.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        androidx.compose.material3.Text(text = song?.name ?: "未选择歌曲")
        androidx.compose.material3.Text(text = song?.artist ?: "")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onToggle) { Text(if (isPlaying) "暂停" else "播放") }
            Button(onClick = onClose) { Text("返回") }
        }
    }
}

