package com.pemrogandroid.uas_

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pemrogandroid.uas_.api.API
import com.pemrogandroid.uas_.model.BookItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailActivity : AppCompatActivity() {

    private lateinit var judulBuku: TextView
    private lateinit var author: TextView
    private lateinit var description: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailpage)

        judulBuku = findViewById(R.id.judulBuku_)
        author = findViewById(R.id.author_)
        description = findViewById(R.id.description)
        imageView = findViewById(R.id.imageView1)

        val bookId = intent.getStringExtra("BOOK_ID")

        fetchBookDetails(bookId)

        val backButton = findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener { finish() }
    }
    private fun fetchBookDetails(bookId: String?) {
        if (bookId == null) {
            Toast.makeText(this, "Invalid book ID", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://www.googleapis.com/books/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(API::class.java)

                val response = apiService.getBookDetails(bookId,"AIzaSyCGd0ETMvaPt2cvinLhm8_Cf44_Tkj6gNM")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val book = response.body()!!
                        updateUI(book)
                    } else {
                        Toast.makeText(this@DetailActivity, "Failed to load book details", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI(book: BookItem) {
        judulBuku.text = book.volumeInfo.title
        author.text = book.volumeInfo.authors.joinToString(", ")
        description.text = book.volumeInfo.description
        Glide.with(this)
            .load(book.volumeInfo.imageLinks?.thumbnail)
            .into(imageView)
    }


}
