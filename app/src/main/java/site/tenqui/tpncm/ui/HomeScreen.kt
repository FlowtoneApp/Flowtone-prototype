package site.tenqui.tpncm.ui

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
import site.tenqui.tpncm.ui.content.HomeContent
import site.tenqui.tpncm.ui.player.MiniPlayer
import site.tenqui.tpncm.ui.player.PlayerManager
import site.tenqui.tpncm.ui.player.PlayerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, playerManager: PlayerManager) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(BottomTab.HOME) }
    var showPlayer by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleFor(currentTab)) }
            )
        },
        bottomBar = {
            androidx.compose.foundation.layout.Column {
                MiniPlayer(
                    song = viewModel.currentSong.value,
                    isPlaying = viewModel.isPlaying.value,
                    onToggle = {
                        viewModel.currentSong.value?.let { s ->
                            playerManager.setNowPlayingInfo(s.name, s.artist)
                        }
                        playerManager.togglePlay(viewModel.currentSong.value?.path)
                        viewModel.setPlaying(playerManager.isPlaying())
                    },
                    onOpenPlayer = { showPlayer = true }
                )
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
            if (showPlayer) {
                PlayerScreen(
                    song = viewModel.currentSong.value,
                    isPlaying = viewModel.isPlaying.value,
                    onToggle = {
                        viewModel.currentSong.value?.let { s ->
                            playerManager.setNowPlayingInfo(s.name, s.artist)
                        }
                        playerManager.togglePlay(viewModel.currentSong.value?.path)
                        viewModel.setPlaying(playerManager.isPlaying())
                    },
                    onClose = { showPlayer = false },
                    padding = padding
                )
            } else {
                when (currentTab) {
                    BottomTab.HOME ->
                        HomeContent(
                            viewModel = viewModel,
                            padding = padding,
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            playerManager = playerManager
                        )
                    BottomTab.SEARCH ->
                        SearchScreen(padding = padding)
                    BottomTab.PROFILE ->
                        ProfilesScreen(viewModel = viewModel, padding = padding)
                }
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