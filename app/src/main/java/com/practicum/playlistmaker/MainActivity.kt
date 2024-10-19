package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.searchButton)
        val mediaButton = findViewById<Button>(R.id.mediaButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        searchButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Заглушка кнопки поиска", Toast.LENGTH_SHORT).show()
        }

        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Заглушка кнопки медиа", Toast.LENGTH_SHORT).show()
        }

        val settingsClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Заглушка для кнопки настроек", Toast.LENGTH_SHORT).show()
            }
        }

        settingsButton.setOnClickListener(settingsClickListener)
    }
}