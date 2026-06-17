# WearTwitch

WearTwitch is a lightweight, high-performance Twitch chat client specifically designed for **Wear OS**. Stay connected with your favorite Twitch communities directly from your wrist.

https://github.com/user-attachments/assets/ad06896a-5a2b-4742-9bdb-1f1ebb692eba

## Features

- **Real-time Chat**: Connect to any Twitch channel and read chat in real-time via WebSockets.
- **Rich Emote Support**: Complete support for:
    - Twitch Native Emotes
    - BetterTTV (BTTV)
    - FrankerFaceZ (FFZ)
    - 7TV
- **Channel Management**: Easily add and switch between multiple Twitch channels.
- **Optimized for Wear OS**:
    - Built with **Compose for Wear OS (Material 3)**.
    - Smooth scrolling with `ScalingLazyColumn`.
    - Intuitive navigation with swipe-to-dismiss support.
- **Privacy Focused**: Connect anonymously without needing to log in or share your Twitch
  credentials.

## Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose for Wear OS](https://developer.android.com/training/wearables/compose) (
  Material 3)
- **Networking**: [OkHttp](https://square.github.io/okhttp/) for WebSocket and HTTP requests.
- **Local Storage**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for
  persistent channel management.
- **Architecture**: MVVM with Kotlin Coroutines and Flow.
