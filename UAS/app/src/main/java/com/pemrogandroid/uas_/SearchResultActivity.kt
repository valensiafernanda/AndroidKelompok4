package com.pemrogandroid.uas_

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pemrogandroid.uas_.adapter.SearchResultAdapter
import com.pemrogandroid.uas_.model.VolumeInfo
import androidx.appcompat.app.AppCompatActivity
import com.pemrogandroid.uas_.api.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchResultActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchResultAdapter
    private var bookList = mutableListOf<VolumeInfo>()
    private lateinit var api: API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchresult)

        recyclerView = findViewById(R.id.mRvCategory)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchResultAdapter(emptyList())
        recyclerView.adapter = adapter

        val query = intent.getStringExtra("QUERY")
        query?.let {
            fetchSearchResults(it)
        }

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }
    }

    private fun fetchSearchResults(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(API::class.java)

        val apiKey = "AIzaSyCGd0ETMvaPt2cvinLhm8_Cf44_Tkj6gNM"

        CoroutineScope(Dispatchers.IO).launch {
            val response = api.searchBooks(query, 20, apiKey)
            if (response.isSuccessful) {
                val booksResponse = response.body()
                booksResponse?.items?.let {
                    bookList.clear()
                    for (bookItem in it) {
                        bookList.add(bookItem.volumeInfo)
                    }
                    withContext(Dispatchers.Main) {
                        adapter.updateData(it)
                    }
                }
            } else {

            }
        }
    }}