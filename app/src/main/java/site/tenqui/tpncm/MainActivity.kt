package site.tenqui.tpncm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import site.tenqui.tpncm.ui.AppRoot
import site.tenqui.tpncm.ui.HomeScreen
import site.tenqui.tpncm.ui.HomeViewModel
import site.tenqui.tpncm.ui.theme.MyMusicApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyMusicApplicationTheme {
                AppRoot()
            }
        }
    }
}
