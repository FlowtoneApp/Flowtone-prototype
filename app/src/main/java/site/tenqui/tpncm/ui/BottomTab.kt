package site.tenqui.tpncm.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTab(
    val title: String,
    val icon: ImageVector
) {
    LIBRARY("库", Icons.Default.LibraryMusic),
    EXPLORE("探索", Icons.Default.Explore),
    SEARCH("搜索", Icons.Default.Search),
    PROFILE("我的", Icons.Default.Person),
}