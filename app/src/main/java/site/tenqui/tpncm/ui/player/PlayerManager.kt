package site.tenqui.tpncm.ui.player

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import site.tenqui.tpncm.model.Song

class PlayerManager(
    context: Context
) {
    private val player = ExoPlayer.Builder(context).build()

    fun play(song: Song){
        TODO("播放功能")
        player.play()
    }

    fun pause(){
        TODO("暂停功能")
        player.pause()
    }
    //TODO:播放状态多着呢
}