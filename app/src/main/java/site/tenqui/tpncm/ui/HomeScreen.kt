package site.tenqui.tpncm.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import site.tenqui.tpncm.ui.content.LibraryScreen
import site.tenqui.tpncm.ui.player.PlayerDock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    // ========== 状态 ==========
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(BottomTab.LIBRARY) }

    // ========== 权限 ==========
    val context = LocalContext.current

    val audioPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            @Suppress("DEPRECATION")
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, audioPermission) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            viewModel.reloadSongs()
        }
    }

    // 进入库页时自动申请（只在 LaunchedEffect 里 launch）
    LaunchedEffect(currentTab) {
        if (currentTab == BottomTab.LIBRARY) {
            hasPermission = ContextCompat.checkSelfPermission(context, audioPermission) ==
                    PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                permissionLauncher.launch(audioPermission)
            } else {
                viewModel.reloadSongs()
            }
        }
    }

    // ========== UI ==========
    Scaffold(
        topBar = { TopAppBar(title = { Text(titleFor(currentTab)) }) },
        bottomBar = {
            Column {
                PlayerDock(
                    onCoverClick = { /* TODO */ },
                    onPrev = {},
                    onPlayPause = {},
                    onNext = {},
                )
                NavigationBar {
                    BottomTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = currentTab == tab,
                            onClick = { currentTab = tab },
                            label = { Text(tab.title) },
                            alwaysShowLabel = true,
                            icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) }
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        when (currentTab) {
            BottomTab.LIBRARY -> LibraryScreen(
                viewModel = viewModel,
                padding = padding,
                snackbarHostState = snackbarHostState,
                scope = scope,
                hasPermission = hasPermission,
                onGoGrant = { context.openAppSettings() }
            )

            BottomTab.EXPLORE -> ExploreScreen(padding = padding)
            BottomTab.SEARCH -> SearchScreen(padding = padding)
            BottomTab.PROFILE -> ProfilesScreen(padding = padding)
        }
    }
}

private fun titleFor(tab: BottomTab): String = when (tab) {
    BottomTab.LIBRARY -> "库"
    BottomTab.EXPLORE -> "探索"
    BottomTab.SEARCH -> "搜索"
    BottomTab.PROFILE -> "我的"
}

// 跳到本 App 的系统设置页
private fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}