package site.tenqui.tpncm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import site.tenqui.tpncm.ui.content.HomeContent
import site.tenqui.tpncm.ui.player.PlayerDock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(BottomTab.HOME) }
    var navBarHeightPx by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleFor(currentTab)) }
            )
        },
        bottomBar = {
            Column {
                PlayerDock(
                    onCoverClick = {
                        // TODO：跳转 overlay
                    },
                    onPrev = {},
                    onPlayPause = {},
                    onNext = {},
                )
                Box(
                    modifier = Modifier.onGloballyPositioned{ coordinates ->
                        navBarHeightPx = coordinates.size.height
                    }
                ) {
                    NavigationBar {
                        BottomTab.entries.forEach { tab ->
                            NavigationBarItem(
                                selected = currentTab == tab,
                                onClick = { currentTab = tab },
                                label = { Text(tab.title) },
                                alwaysShowLabel = true,
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title
                                    )
                                }
                            )
                        }
                    }
                }
        }
    },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
            when (currentTab) {
                BottomTab.HOME ->
                    HomeContent(
                        viewModel = viewModel,
                        padding = padding,
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )

                BottomTab.SEARCH ->
                    SearchScreen(padding = padding)

                BottomTab.PROFILE ->
                    ProfilesScreen(padding = padding)
            }
        }
    }

private fun titleFor(tab: BottomTab): String {
    return when (tab) {
        BottomTab.HOME -> "推荐"
        BottomTab.SEARCH -> "搜索"
        BottomTab.PROFILE -> "我的"
    }
}