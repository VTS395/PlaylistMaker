package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.api.RetrofitClient
import com.practicum.playlistmaker.api.Track
import com.practicum.playlistmaker.api.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var textValue: String? = TEXT_DEF
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rwTrackList: RecyclerView
    private lateinit var placeholder: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var updateResponse: Button

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)
    private var lastQuery = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_AMOUNT, textValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textValue = savedInstanceState.getString(TEXT_AMOUNT, TEXT_DEF)
        findViewById<EditText>(R.id.inputEditText).setText(textValue)
    }

    companion object {
        const val TEXT_AMOUNT = "TEXT_AMOUNT"
        const val TEXT_DEF = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById<EditText>(R.id.inputEditText)
        clearButton = findViewById<ImageView>(R.id.clearIcon)
        rwTrackList = findViewById<RecyclerView>(R.id.rwTrackList)
        placeholder = findViewById<ImageView>(R.id.placeholder)
        placeholderMessage = findViewById<TextView>(R.id.PlaceholderMessage)
        updateResponse = findViewById<Button>(R.id.updateResponse)


        val backButton = findViewById<MaterialToolbar>(R.id.backButton)
        backButton.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            setPlaceholderVisibility(true)
            hideKeyboard()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()

                textValue = s?.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(textWatcher)
        rwTrackList.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearch(inputEditText.text.toString())
            }
            false
        }

        updateResponse.setOnClickListener() {
            if (lastQuery.isNotEmpty()) {
                trackSearch(lastQuery)
            } else {
                Toast.makeText(applicationContext, R.string.nothing_to_search, Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun trackSearch(input: String) {
        RetrofitClient.itunesService.search(input).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    tracks.clear()
                    setPlaceholderVisibility(true)

                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                    }
                    if (tracks.isEmpty()) {
                        showNoResults()
                    }
                } else {
                    showError(input)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                showError(input)
            }
        })
    }

    private fun showError(input: String) {
        setPlaceholderVisibility(false)
        placeholder.setImageResource(R.drawable.ic_no_internet)
        placeholderMessage.setText(R.string.connection_problems)
        lastQuery = input
    }

    private fun showNoResults() {
        tracks.clear()
        setPlaceholderVisibility(false)
        updateResponse.visibility = View.GONE
        placeholder.setImageResource(R.drawable.ic_notning_found)
        placeholderMessage.setText(R.string.nothing_found)
    }

    private fun setPlaceholderVisibility(isNotVisible: Boolean) {
        if (isNotVisible) {
            rwTrackList.visibility = View.VISIBLE
            placeholder.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            updateResponse.visibility = View.GONE
        } else {
            rwTrackList.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE
            updateResponse.visibility = View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
    }
}