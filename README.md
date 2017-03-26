# DASHPlayer

Simple player library supporting reproduction of DASH streams build around ExoPlayer.

Fetures:
1. Open a DASH URL for playback
2. Play/Pause during playbac
3. Disable adaptive streaming and play a fixed quality


Components:
1. Wrapper - library build around ExoPlayer exposing methods for starting/stoping playback, fetching list of available tracks inside stream and enabling adaptive streaming or streaming of only one profile
2. DemoApp - demo app consuming ExoPlayer Wrapper library

