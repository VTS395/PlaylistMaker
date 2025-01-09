package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val trackNameTextView: TextView = findViewById(R.id.trackName)
        val artistNameTextView: TextView = findViewById(R.id.artistName)
        val trackTimeTextView: TextView = findViewById(R.id.trackTimeMillis)
        val collectionNameTextView: TextView = findViewById(R.id.collectionName)
        val releaseDateTextView: TextView = findViewById(R.id.releaseDate)
        val primaryGenreNameTextView: TextView = findViewById(R.id.primaryGenreName)
        val countryTextView: TextView = findViewById(R.id.country)
        val albumCoverImageView: ImageView = findViewById(R.id.albumCover)

        backButton.setOnClickListener(){
            finish()
        }

        trackNameTextView.text = intent.getStringExtra("trackName")
        artistNameTextView.text = intent.getStringExtra("artistName")
        trackTimeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(intent.getLongExtra("trackTimeMillis", 0))
        collectionNameTextView.text = intent.getStringExtra("collectionName")
        releaseDateTextView.text = intent.getStringExtra("releaseDate")
        primaryGenreNameTextView.text = intent.getStringExtra("primaryGenreName")
        countryTextView.text = intent.getStringExtra("country")

        Glide.with(this)
            .load(intent.getStringExtra("artworkUrl100"))
            .fitCenter()
            .placeholder(R.drawable.album_cover_placeholder)
            .into(albumCoverImageView)
    }
}