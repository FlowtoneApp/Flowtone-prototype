package site.tenqui.tpncm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppRoot(){
    val viewModel: HomeViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()){

        HomeScreen(viewModel = viewModel)

    }
}