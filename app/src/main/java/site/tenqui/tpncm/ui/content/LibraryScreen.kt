package site.tenqui.tpncm.ui.content

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import site.tenqui.tpncm.ui.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: HomeViewModel,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    hasPermission: Boolean,
    onGoGrant: () -> Unit, // HomeScreen 传 context.openAppSettings()
) {
    val context = LocalContext.current
    val songs by viewModel.songs

    // 下拉刷新
    val pullState = rememberPullToRefreshState()

    // 进入库页：有权限就扫一次（避免空白）
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            viewModel.reloadSongs()
        }
    }

    // 下拉刷新触发：有权限才扫
    LaunchedEffect(pullState.isRefreshing) {
        if (!pullState.isRefreshing) return@LaunchedEffect

        if (hasPermission) {
            viewModel.reloadSongs()
        }
        pullState.endRefresh()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // 1) 没权限
        if (!hasPermission) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("音频权限被拒绝")
                Text("请在设置中允许后再扫描音乐")

                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = onGoGrant
                ) {
                    Text("去授权")
                }
            }

            // 仍然允许下拉，但只显示动画，不做扫描（可选）
            PullToRefreshContainer(
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            return@Box
        }

        // 2) 有权限但空列表
        if (songs.isEmpty() && !pullState.isRefreshing) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("当前目录下无音乐")
                Text("下拉可以重新扫描")

                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { pullState.startRefresh() }
                ) {
                    Text("重新扫描")
                }
            }
        } else {
            // 3) 有数据：交给纯列表组件渲染
            SongListContent(
                songs = songs,
                padding = PaddingValues(0.dp), // 外层已经 padding(padding) 了
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        }

        // 下拉刷新头
        PullToRefreshContainer(
            state = pullState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // 刷新时可选：居中加载
        if (pullState.isRefreshing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}