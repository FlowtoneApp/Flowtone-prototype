package site.tenqui.tpncm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ProfilesScreen(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        Text("我的")
        Spacer(modifier = Modifier.height(12.dp))
        Text("BatterNetEaseCloudMusicClient")
        Text("Ver: 0.1.0")
        Spacer(modifier = Modifier.height(12.dp))
        Text("杂七杂八")
    }
}
