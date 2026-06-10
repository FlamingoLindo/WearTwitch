/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up-to-date changes to the libraries and their usages.
 */

package com.example.weartwitch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.example.weartwitch.presentation.composables.EmptyChannelList
import com.example.weartwitch.presentation.theme.WearTwitchTheme
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.weartwitch.presentation.screens.AddChannelScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp("Android")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    WearTwitchTheme {
        val navController = rememberSwipeDismissableNavController()
        AppScaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    val listState = rememberTransformingLazyColumnState()
                    ScreenScaffold(scrollState = listState) { contentPadding ->
                        EmptyChannelList(onAddChannel = { navController.navigate("add_channel") })
                    }
                }
                composable("add_channel") {
                    AddChannelScreen()
                }
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}