package site.tenqui.tpncm.ui.player

import androidx.compose.runtime.mutableStateOf
import site.tenqui.tpncm.model.Song

//全局播放状态
object PlayerState{

    //是否正在播放
    val isPlaying = mutableStateOf(false)

    //当前是否有歌曲
    val hasTrack = mutableStateOf(true)

    //是否有下一首
    val hasNext = mutableStateOf(false)

    //当前歌曲
    val currentSong = mutableStateOf<Song?>(null)
}