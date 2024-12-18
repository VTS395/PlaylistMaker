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

    private lateinit var searchHistory: SearchHistory

    private var textValue: String? = TEXT_DEF
    private lateinit var searchField: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerViewTrackList: RecyclerView
    private lateinit var placeholder: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var updateResponse: Button
    private lateinit var historyHeader: TextView
    private lateinit var historyClear: Button

    private val tracks = ArrayList<Track>()
    private val history = ArrayList<Track>()

    private val searchAdapter = TrackAdapter(tracks) {
        searchHistory.addTrackToHistory(it)
        loadHistory()
    }

    private val historyAdapter = TrackAdapter(history) {
        Toast.makeText(applicationContext, "Player will be here later", Toast.LENGTH_SHORT).show()
    }

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

        val sharedPreferences = getSharedPreferences("searchPrefs", Context.MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        searchField = findViewById<EditText>(R.id.inputEditText)
        clearButton = findViewById<ImageView>(R.id.clearIcon)
        recyclerViewTrackList = findViewById<RecyclerView>(R.id.rwTrackList)
        placeholder = findViewById<ImageView>(R.id.placeholder)
        placeholderMessage = findViewById<TextView>(R.id.PlaceholderMessage)
        updateResponse = findViewById<Button>(R.id.updateResponse)
        historyHeader = findViewById<TextView>(R.id.historyHeader)
        historyClear = findViewById<Button>(R.id.historyClear)

        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchField.text.isEmpty() && history.isNotEmpty()) {
                setHistoryVisibility(true)
            } else {
                setHistoryVisibility(false)
            }
        }

        historyClear.setOnClickListener {
            searchHistory.clearHistory()
            history.clear()
            historyAdapter.notifyDataSetChanged()
            setHistoryVisibility(false)
        }

        val backButton = findViewById<MaterialToolbar>(R.id.backButton)
        backButton.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchField.setText("")
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
            setPlaceholderVisibility(false)
            hideKeyboard()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                textValue = s?.toString()

                if (searchField.hasFocus() && s?.isEmpty() == true && history.isNotEmpty()) {
                    setHistoryVisibility(true)
                    loadHistory()
                } else {
                    setHistoryVisibility(false)
                }

                if(s.isNullOrEmpty()) {
                    setPlaceholderVisibility(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchField.addTextChangedListener(textWatcher)
        recyclerViewTrackList.adapter = searchAdapter

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchField.text.isNotEmpty()){
                    trackSearch(searchField.text.toString())
                }
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
        loadHistory()
    }

    fun loadHistory() {
        history.clear()
        history.addAll(searchHistory.getSearchHistory())
        historyAdapter.notifyDataSetChanged()
    }

    private fun trackSearch(input: String) {
        RetrofitClient.itunesService.search(input).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    tracks.clear()
                    setPlaceholderVisibility(false)

                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                        recyclerViewTrackList.adapter = searchAdapter
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
        setPlaceholderVisibility(true)
        placeholder.setImageResource(R.drawable.ic_no_internet)
        placeholderMessage.setText(R.string.connection_problems)
        lastQuery = input
    }

    private fun showNoResults() {
        tracks.clear()
        setPlaceholderVisibility(true)
        updateResponse.visibility = View.GONE
        placeholder.setImageResource(R.drawable.ic_notning_found)
        placeholderMessage.setText(R.string.nothing_found)
    }

    private fun setPlaceholderVisibility(isVisible: Boolean) {
        if (isVisible) {
            recyclerViewTrackList.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE
            updateResponse.visibility = View.VISIBLE
        } else {
            recyclerViewTrackList.visibility = View.VISIBLE
            placeholder.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            updateResponse.visibility = View.GONE
        }
    }

    private fun setHistoryVisibility(isVisible: Boolean) {
        recyclerViewTrackList.adapter = historyAdapter
        if (isVisible) {
            historyHeader.visibility = View.VISIBLE
            recyclerViewTrackList.visibility = View.VISIBLE
            historyClear.visibility = View.VISIBLE
        } else {
            historyHeader.visibility = View.GONE
            recyclerViewTrackList.visibility = View.GONE
            historyClear.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
    }
}