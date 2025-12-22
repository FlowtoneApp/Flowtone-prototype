package site.tenqui.tpncm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import android.Manifest
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
 
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import site.tenqui.tpncm.ui.HomeScreen
import site.tenqui.tpncm.ui.HomeViewModel
import site.tenqui.tpncm.ui.player.PlayerManager
import site.tenqui.tpncm.ui.theme.MyMusicApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyMusicApplicationTheme {
                val context = LocalContext.current
                val playerManager = remember { PlayerManager(context) }
                val viewModel: HomeViewModel = viewModel()
                val notifLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }
                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        val granted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                        if (!granted) {
                            notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                    viewModel.loadAndScanSavedTree(context)
                }
                HomeScreen(viewModel = viewModel, playerManager = playerManager)
            }
        }
    }
}
