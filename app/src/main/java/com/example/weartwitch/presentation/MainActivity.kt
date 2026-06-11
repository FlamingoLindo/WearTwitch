/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up-to-date changes to the libraries and their usages.
 */

package com.example.weartwitch.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
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
import com.example.weartwitch.presentation.composables.readChannels
import com.example.weartwitch.presentation.screens.AddChannelScreen
import com.example.weartwitch.presentation.screens.Channel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.weartwitch.presentation.composables.saveChannel
import com.example.weartwitch.presentation.screens.SelectChannel

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
    val context = LocalContext.current
    val navController = rememberSwipeDismissableNavController()
    var channels by remember { mutableStateOf(emptySet<String>()) }

    WearTwitchTheme {
        LaunchedEffect(Unit) {
            channels = readChannels(context)
            saveChannel(context, "flamingo_lindo")
            saveChannel(context, "forsen")
            saveChannel(context, "pleaseendmyloneliness")
            saveChannel(context, "moonmoon")

            if (channels.isEmpty()) {
                navController.navigate("no_channel") {
                    popUpTo("start_up") { inclusive = true }
                }
            } else {
                navController.navigate("channel/${channels.first()}") {
                    popUpTo("start_up") { inclusive = true }
                }
            }
        }

        AppScaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "start_up"
            ) {
                composable("start_up") { }

                composable("no_channel") {
                    val listState = rememberTransformingLazyColumnState()
                    ScreenScaffold(scrollState = listState) { contentPadding ->
                        EmptyChannelList(onAddChannel = { navController.navigate("add_channel") })
                    }
                }

                composable("add_channel") {
                    AddChannelScreen()
                }

                composable("channel/{name}") { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""

                    val listState = rememberTransformingLazyColumnState()

                    ScreenScaffold(
                        scrollState = listState
                    ) { contentPadding ->
                        Channel(
                            name = name,
                            modifier = Modifier.padding(contentPadding),
                            onSelectChannel = { navController.navigate("select_channel") }
                        )
                    }
                }

                composable("select_channel") {
                    SelectChannel(
                        channels,
                        onAddChannel = { navController.navigate("add_channel") },
                        onSelectChannel = { channelName ->
                            navController.navigate("channel/$channelName")
                        })
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