/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up-to-date changes to the libraries and their usages.
 */

package com.example.weartwitch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
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
import com.example.weartwitch.presentation.screens.Channel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weartwitch.presentation.screens.SelectChannel
import com.example.weartwitch.presentation.screens.StartUp
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }

}

@Composable
fun WearApp() {
    val context = LocalContext.current
    val navController = rememberSwipeDismissableNavController()
    val channels by getChannels(context).collectAsStateWithLifecycle(emptySet())
    val viewModel: TwitchViewModel = viewModel()

    WearTwitchTheme {
        LaunchedEffect(Unit) {
            val currentChannels = getChannels(context).first()
            if (currentChannels.isEmpty()) {
                // Pre-seed some popular channels if none exist
                saveChannel(context, "flamingo_lindo")
                saveChannel(context, "forsen")
                saveChannel(context, "moonmoon")

                navController.navigate("no_channel") {
                    popUpTo("start_up") { inclusive = true }
                }
            } else {
                navController.navigate("channel/${currentChannels.first()}") {
                    popUpTo("start_up") { inclusive = true }
                }
            }
        }

        AppScaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "start_up"
            ) {
                composable("start_up") {
                    ScreenScaffold {
                        StartUp()
                    }
                }

                composable("no_channel") {
                    val listState = rememberScalingLazyListState()
                    ScreenScaffold(scrollState = listState) {
                        EmptyChannelList(onAddChannel = { navController.navigate("add_channel") })
                    }
                }

                composable("add_channel") {
                    ScreenScaffold {
                        AddChannelScreen(onChannelAdd = { channelName ->
                            navController.navigate("channel/$channelName") {
                                popUpTo("add_channel") { inclusive = true }
                            }
                        })
                    }
                }

                composable("channel/{name}") { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val listState = rememberScalingLazyListState()
                    val messages by viewModel.messages.collectAsStateWithLifecycle()
                    LaunchedEffect(name) {
                        viewModel.connect(name)
                    }

                    ScreenScaffold(
                        scrollState = listState
                    ) { contentPadding ->
                        Channel(
                            name = name,
                            modifier = Modifier.padding(contentPadding),
                            messages = messages,
                            onSelectChannel = { navController.navigate("select_channel") },
                            listState = listState
                        )
                    }
                }

                composable("select_channel") {
                    val listState = rememberScalingLazyListState()
                    ScreenScaffold(scrollState = listState) {
                        SelectChannel(
                            channels,
                            onAddChannel = { navController.navigate("add_channel") },
                            onSelectChannel = { channelName ->
                                navController.navigate("channel/$channelName")
                            },
                            listState = listState
                        )
                    }
                }
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    WearApp()
}