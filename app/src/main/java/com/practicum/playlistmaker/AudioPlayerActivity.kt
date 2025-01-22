package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
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

        backButton.setNavigationOnClickListener {
            finish()
        }

        trackNameTextView.text = intent.getStringExtra(TRACK_NAME)
        artistNameTextView.text = intent.getStringExtra(ARTIST_NAME)
        trackTimeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
            intent.getLongExtra(
                TRACK_TIME_MILLIS,
                0
            )
        )
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

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}