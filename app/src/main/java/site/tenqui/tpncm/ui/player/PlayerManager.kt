package site.tenqui.tpncm.ui.player

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import site.tenqui.tpncm.model.Song

class PlayerManager{

    fun play(song: Song? = null){
        if (song != null){
            PlayerState.currentSong.value = song
            PlayerState.hasTrack.value = true
        }
        PlayerState.isPlaying.value = true
    }
    fun pause(){
        PlayerState.isPlaying.value = false
    }
    fun toggle() {
        PlayerState.isPlaying.value = !PlayerState.isPlaying.value
    }
    fun next(){
        //todo
    }
    fun previous(){
        //todo
    }
}