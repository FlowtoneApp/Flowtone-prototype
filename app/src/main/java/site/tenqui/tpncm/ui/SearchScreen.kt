package site.tenqui.tpncm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(padding: PaddingValues) {
    var keyword by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = keyword,
            onValueChange = { keyword = it },
            label = { Text("搜索歌曲/歌手") },
            singleLine = true,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("输入: $keyword")
        Spacer(modifier = Modifier.height(8.dp))
        Text("之后的事")
    }
}
