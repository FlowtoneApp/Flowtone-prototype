package site.tenqui.tpncm.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTab(
    val title: String,
    val icon: ImageVector
) {
    HOME("推荐", Icons.Filled.Home),
    SEARCH("搜索", Icons.Filled.Search),
    PROFILE("我的", Icons.Filled.Person)
}
