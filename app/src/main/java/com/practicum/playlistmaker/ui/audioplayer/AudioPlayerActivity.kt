package com.practicum.playlistmaker.ui.audioplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_NAME = "trackName"
        const val ARTIST_NAME = "artistName"
        const val TRACK_TIME_MILLIS = "trackTimeMillis"
        const val ARTWORK_URL = "artworkUrl100"
        const val COLLECTION_NAME = "collectionName"
        const val RELEASE_DATE = "releaseDate"
        const val PRIMARY_GENRE_NAME = "primaryGenreName"
        const val COUNTRY = "country"
        const val PREVIEW_URL = "previewUrl"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DEBOUNCE_DELAY = 500L
    }

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var btnPlay: ImageView
    private var previewUrl: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var currentTimeTextView: TextView

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                currentTimeTextView.text = dateFormat.format(mediaPlayer.currentPosition)
                handler.postDelayed(this, DEBOUNCE_DELAY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backButton = findViewById<MaterialToolbar>(R.id.backButton)
        val trackNameTextView: TextView = findViewById(R.id.trackName)
        val artistNameTextView: TextView = findViewById(R.id.artistName)
        val trackTimeTextView: TextView = findViewById(R.id.trackTimeMillis)
        val collectionNameTextView: TextView = findViewById(R.id.collectionName)
        val releaseDateTextView: TextView = findViewById(R.id.releaseDate)
        val primaryGenreNameTextView: TextView = findViewById(R.id.primaryGenreName)
        val countryTextView: TextView = findViewById(R.id.country)
        val albumCoverImageView: ImageView = findViewById(R.id.albumCover)
        btnPlay = findViewById(R.id.playButton)
        currentTimeTextView = findViewById(R.id.currentTime)

        previewUrl = intent.getStringExtra(PREVIEW_URL)

        preparePlayer()

        btnPlay.setOnClickListener() {
            playbackControl()
        }

        backButton.setNavigationOnClickListener {
            finish()
        }

        trackNameTextView.text = intent.getStringExtra(TRACK_NAME)
        artistNameTextView.text = intent.getStringExtra(ARTIST_NAME)
        collectionNameTextView.text = intent.getStringExtra(COLLECTION_NAME)
        releaseDateTextView.text = intent.getStringExtra(RELEASE_DATE)
        primaryGenreNameTextView.text = intent.getStringExtra(PRIMARY_GENRE_NAME)
        countryTextView.text = intent.getStringExtra(COUNTRY)

        val cornerRadius = dpToPx(8f, this)

        Glide.with(this)
            .load(intent.getStringExtra("artworkUrl100"))
            .fitCenter()
            .placeholder(R.drawable.album_cover_placeholder)
            .transform(RoundedCorners(cornerRadius))
            .into(albumCoverImageView)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateProgressRunnable)
        mediaPlayer.release()
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            btnPlay.setImageResource(R.drawable.ic_play)
            currentTimeTextView.text = dateFormat.format(0L)
            playerState = STATE_PREPARED
            handler.removeCallbacks(updateProgressRunnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlay.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        handler.post(updateProgressRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}