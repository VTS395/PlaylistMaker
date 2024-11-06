package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val shareButton = findViewById<FrameLayout>(R.id.shareApp)
        val supportButton = findViewById<FrameLayout>(R.id.support)
        val aggrimentButton = findViewById<FrameLayout>(R.id.aggriment)

        backButton.setOnClickListener{
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        shareButton.setOnClickListener {
            val message = "https://practicum.yandex.ru/android-developer/"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val title = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("tsoy@gmlab.pro"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportIntent)
        }

        aggrimentButton.setOnClickListener {
            val url = "https://yandex.ru/legal/practicum_offer/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}