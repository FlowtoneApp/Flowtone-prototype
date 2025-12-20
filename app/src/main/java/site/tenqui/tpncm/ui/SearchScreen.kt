package site.tenqui.tpncm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("搜索")
        Text("这里将来是搜索页面")
    }
}
