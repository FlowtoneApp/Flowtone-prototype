package site.tenqui.tpncm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import android.content.Intent


@Composable
fun ProfilesScreen(viewModel: HomeViewModel, padding: PaddingValues) {
    val context = LocalContext.current
    val directoryPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            viewModel.saveTreeUri(context, uri)
            viewModel.scanTree(context, uri)
        }
    }
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
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { directoryPicker.launch(null) }) { Text("选择目录并扫描") }
    }
}
