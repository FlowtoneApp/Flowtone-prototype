package site.tenqui.tpncm.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import site.tenqui.tpncm.data.SongRepository
import site.tenqui.tpncm.model.Song
import android.content.Context
import android.net.Uri
import android.content.SharedPreferences

class HomeViewModel : ViewModel() {

    private val repository = SongRepository()

    private val _songs = mutableStateOf<List<Song>>(repository.getSongs())
    val songs: State<List<Song>> = _songs

    val currentSong = mutableStateOf<Song?>(null)
    val isPlaying = mutableStateOf(false)

    fun setCurrentSong(song: Song) {
        currentSong.value = song
    }

    fun setPlaying(playing: Boolean) {
        isPlaying.value = playing
    }

    fun scanPath(path: String) {
        repository.scanDirectory(path)
        _songs.value = repository.getSongs()
    }

    fun scanTree(context: Context, uri: Uri) {
        repository.scanTree(context, uri)
        _songs.value = repository.getSongs()
    }

    fun saveTreeUri(context: Context, uri: Uri) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("tree_uri", uri.toString()).apply()
    }

    fun loadAndScanSavedTree(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val s = prefs.getString("tree_uri", null)
        if (s != null) {
            try {
                val uri = Uri.parse(s)
                repository.scanTree(context, uri)
                _songs.value = repository.getSongs()
            } catch (_: Exception) {}
        }
    }
}
