package site.tenqui.tpncm.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import site.tenqui.tpncm.data.SongRepository
import site.tenqui.tpncm.model.Song

class HomeViewModel : ViewModel() {

    private val repository = SongRepository()

    private val _songs = mutableStateOf<List<Song>>(repository.getSongs())
    val songs: State<List<Song>> = _songs
}
