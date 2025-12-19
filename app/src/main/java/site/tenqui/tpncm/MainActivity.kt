package site.tenqui.tpncm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import site.tenqui.tpncm.ui.HomeViewModel

enum class BottomTab(val title: String) {
    HOME("推荐"),
    SEARCH("搜索"),
    PROFILE("我的")
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HomeViewModel = viewModel()
            HomeScreen(viewModel)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val songs by viewModel.songs
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(BottomTab.HOME) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentTab) {
                            BottomTab.HOME -> "推荐"
                            BottomTab.SEARCH -> "搜索"
                            BottomTab.PROFILE -> "我的"
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                BottomTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        label = { Text(tab.title) },
                        icon = {} // 先空着，下一步再加 icon
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            when (currentTab) {
                BottomTab.HOME -> {
                    songs.forEach { song ->
                        Text(
                            text = "${song.name} - ${song.artist}",
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "正在播放：${song.name} - ${song.artist}"
                                        )
                                    }
                                }
                        )
                    }
                }

                BottomTab.SEARCH -> {
                    Text("这里是搜索页面（未实现）")
                }

                BottomTab.PROFILE -> {
                    Text("这里是我的页面（未实现）")
                }
            }
        }

    }
}