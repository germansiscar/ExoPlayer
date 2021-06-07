package com.example.exoplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.exoplayerfunctionality.Utils
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var player : SimpleExoPlayer
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }


     fun init(){
        player = SimpleExoPlayer.Builder(this).build()
        player.addListener(PlayerEventListener())

        utils = Utils()

        playerView.setPlayer(player)

        val mediaItem: MediaItem = MediaItem.fromUri("http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8")

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    inner class PlayerEventListener : Player.Listener {

        override fun onPlaybackStateChanged(state: Int) {
            if (player.isPlaying) {
                Log.d("exoPlayerEvent","first play")
                utils.startedVideo(this@MainActivity)
            }
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playWhenReady && player.isPlaying && playbackState != Player.STATE_BUFFERING) {
                Log.d("exoPlayerEvent","resume")
                utils.increaseResumeCounter()
                txtResumeCounter.text = "Times resumed:" +utils.getResumeCounter().toString()
                txtElapsedTime.text = "Time elapsed between pause/resume: " +utils.getElapsedTime().toString()
                txtAllElapsedTime.text = "All Time elapsed between pause/resume: " +utils.getAllElapsedTime().toString()
            }  else if (playWhenReady && playbackState == Player.STATE_ENDED) {
                Log.d("exoPlayerEvent","finish")
                utils.finishedVideo(this@MainActivity)
            } else if(!player.isPlaying && playbackState != Player.STATE_BUFFERING){
                Log.d("exoPlayerEvent","pause")
                utils.increasePauseCounter()
                txtPauseCounter.text = "Times paused:" +utils.getPauseCounter().toString()
            }
        }

        override fun onRenderedFirstFrame() {
            Log.d("exoPlayerEvent","firstFrame")
            utils.firstFrame(this@MainActivity)
        }
    }
}