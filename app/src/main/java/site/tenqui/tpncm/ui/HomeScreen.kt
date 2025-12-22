package site.tenqui.tpncm.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.roundToInt
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
    var moving by remember { mutableStateOf(false) }
    var infoSource by remember { mutableStateOf<Offset?>(null) }
    var infoDest by remember { mutableStateOf<Offset?>(null) }
    val animX = remember { Animatable(0f) }
    val animY = remember { Animatable(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleFor(currentTab)) }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = !showPlayer,
                enter = slideInVertically(animationSpec = tween(durationMillis = 300), initialOffsetY = { it }),
                exit = slideOutVertically(animationSpec = tween(durationMillis = 300), targetOffsetY = { it })
            ) {
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
                        onOpenPlayer = {
                            showPlayer = true
                        },
                        onInfoPositioned = { infoSource = it },
                        showText = !showPlayer
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
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
            Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
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
                        padding = padding,
                        showInfo = !moving,
                        onInfoPositioned = { infoDest = it }
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
                if (moving && viewModel.currentSong.value != null && infoSource != null && infoDest != null) {
                    val x = animX.value
                    val y = animY.value
                    
                    val artistStyle = MaterialTheme.typography.bodyMedium
                    val titleStyle = artistStyle.copy(
                        fontSize = artistStyle.fontSize * 1.5f,
                        fontWeight = FontWeight.Bold
                    )
                    
                    androidx.compose.foundation.layout.Column(
                        modifier = androidx.compose.ui.Modifier.offset { IntOffset(x.roundToInt(), y.roundToInt()) }
                    ) {
                        Text(text = viewModel.currentSong.value!!.name, style = titleStyle)
                        Text(text = viewModel.currentSong.value!!.artist, style = artistStyle)
                    }
                }
            }
            LaunchedEffect(showPlayer, infoSource, infoDest) {
                if (showPlayer && infoSource != null && infoDest != null) {
                    moving = true
                    animX.snapTo(infoSource!!.x)
                    animY.snapTo(infoSource!!.y)
                    // Use easing for the movement
                    animX.animateTo(infoDest!!.x, tween(durationMillis = 400, easing = androidx.compose.animation.core.FastOutSlowInEasing))
                    animY.animateTo(infoDest!!.y, tween(durationMillis = 400, easing = androidx.compose.animation.core.FastOutSlowInEasing))
                    moving = false
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