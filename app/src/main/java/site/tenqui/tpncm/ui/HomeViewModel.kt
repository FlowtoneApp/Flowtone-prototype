package site.tenqui.tpncm.ui

import androidx.lifecycle.ViewModel
import site.tenqui.tpncm.Interface.SongDataSource
import site.tenqui.tpncm.data.SongRepository
import site.tenqui.tpncm.model.Song

class HomeViewModel(
    private val dataSource: SongDataSource = SongRepository()
) : ViewModel() {

    val songs = dataSource.getSongs()
}
