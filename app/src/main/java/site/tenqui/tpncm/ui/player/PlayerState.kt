package site.tenqui.tpncm.ui.player

import site.tenqui.tpncm.model.Song

data class PlayerState(
    val isPlaying: Boolean = false,
    val currentSong: Song? = null
    // TODO: 状态/队列/进度/播放模式
)