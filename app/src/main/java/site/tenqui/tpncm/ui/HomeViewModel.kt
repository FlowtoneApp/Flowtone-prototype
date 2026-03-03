package site.tenqui.tpncm.ui

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import site.tenqui.tpncm.Interface.SongDataSource
import site.tenqui.tpncm.data.MusicFolderSongDataSource
import site.tenqui.tpncm.data.SongRepository
import site.tenqui.tpncm.model.Song

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // 用 application context 构造本地数据源
    private val localSource: SongDataSource =
        MusicFolderSongDataSource(application)

    private val repository = SongRepository(
        listOf(localSource)
    )

    private val _songs = mutableStateOf<List<Song>>(emptyList())
    val songs: State<List<Song>> = _songs

    fun reloadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getSongs()
            _songs.value = list
        }
    }
}