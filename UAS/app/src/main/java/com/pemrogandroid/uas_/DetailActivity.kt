
package com.pemrogandroid.uas_

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pemrogandroid.uas_.api.API
import com.pemrogandroid.uas_.model.Book
import com.pemrogandroid.uas_.model.BookItem
import com.pemrogandroid.uas_.model.UserBook
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
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var quantityTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailpage)

        judulBuku = findViewById(R.id.judulBuku_)
        author = findViewById(R.id.author_)
        description = findViewById(R.id.description)
        imageView = findViewById(R.id.imageView1)
        quantityTextView = findViewById(R.id.jlhbuku_)
        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("books")

        val bookId = intent.getStringExtra("BOOK_ID")
        bookId?.let {
            fetchBookDetails(it)
        }

        val pinjamButton = findViewById<Button>(R.id.pinjambtn)
        pinjamButton.setOnClickListener {
            val bookId = intent.getStringExtra("BOOK_ID")
            bookId?.let {
                pinjamBuku(it)
            }
        }

        val backButton = findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener { finish() }
    }

    private fun checkBookExistence(bookId: String, bookItem: BookItem) {
        reference.child(bookId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val book = dataSnapshot.getValue(Book::class.java)
                    if (book != null) {
                        updateUIFromDatabase(book)
                    }
                } else {
                    addNewBookToDatabase(bookId, bookItem)
                    updateUI(bookItem)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@DetailActivity, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addNewBookToDatabase(bookId: String, bookItem: BookItem) {
        val newBook = Book(
            id = bookId,
            title = bookItem.volumeInfo.title ?: "Unknown Title",
            author = bookItem.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author",
            description = bookItem.volumeInfo.description ?: "No Description",
            availableQuantity = 10,
            imageUrl = bookItem.volumeInfo.imageLinks?.thumbnail ?: ""
        )
        reference.child(bookId).setValue(newBook).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUIFromDatabase(newBook)
            } else {
                Toast.makeText(this@DetailActivity, "Failed to add book to database", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUIFromDatabase(book: Book) {
        if (!isDestroyed) {
            judulBuku.text = book.title
            author.text = book.author
            description.text = book.description
            Glide.with(this)
                .load(book.imageUrl)
                .into(imageView)

            quantityTextView.text = "${book.availableQuantity}"
        }
    }

    private fun fetchBookDetails(bookId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://www.googleapis.com/books/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(API::class.java)
                val response = apiService.getBookDetails(bookId, "AIzaSyCGd0ETMvaPt2cvinLhm8_Cf44_Tkj6gNM")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val bookItem = response.body()!!
                        checkBookExistence(bookId, bookItem)
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

    private fun pinjamBuku(bookId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        currentUserId?.let { userId ->
            val userBooksRef = FirebaseDatabase.getInstance().getReference("user_books").child(userId)
            val bookRef = FirebaseDatabase.getInstance().getReference("books").child(bookId)

            bookRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val book = dataSnapshot.getValue(Book::class.java)
                    if (book != null && book.availableQuantity > 0) {
                        bookRef.runTransaction(object : Transaction.Handler {
                            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                val bookData = mutableData.getValue(Book::class.java)
                                if (bookData != null && bookData.availableQuantity > 0) {
                                    // Kurangi jumlah buku yang tersedia
                                    bookData.availableQuantity -= 1
                                    mutableData.value = bookData
                                    return Transaction.success(mutableData)
                                }
                                return Transaction.abort()
                            }

                            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                                if (committed) {
                                    val newUserBookRef = userBooksRef.child(bookId)
                                    newUserBookRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(userBookDataSnapshot: DataSnapshot) {
                                            if (userBookDataSnapshot.exists()) {
                                                val userBook = userBookDataSnapshot.getValue(UserBook::class.java)
                                                userBook?.let {
                                                    userBook.jumlahBuku += 1
                                                    newUserBookRef.setValue(userBook)
                                                }
                                            } else {
                                                val newUserBook = UserBook(
                                                    bookId,
                                                    book.title,
                                                    book.imageUrl,
                                                    jumlahBuku = 1,
                                                    borrowedAt = System.currentTimeMillis(),
                                                    isConfirmed = true
                                                )
                                                newUserBookRef.setValue(newUserBook)
                                            }
                                            updateUIFromDatabase(book)
                                        }


                                        override fun onCancelled(databaseError: DatabaseError) {
                                        }
                                    })

                                    Toast.makeText(this@DetailActivity, "Buku berhasil dipinjam", Toast.LENGTH_SHORT).show()
                                    updateUIFromDatabase(book)
                                } else {
                                    if (databaseError != null) {
                                        Log.e("FirebaseTransaction", "Transaction failed with error: ${databaseError.message}")
                                    }
                                    Toast.makeText(this@DetailActivity, "Coba lagi", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    } else {

                        Toast.makeText(this@DetailActivity, "Coba lagi", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseDatabase", "onCancelled: ${databaseError.message}")
                    Toast.makeText(this@DetailActivity, "Gagal memproses permintaan", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun updateUI(book: BookItem) {
        judulBuku.text = book.volumeInfo.title
        author.text = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author"
        description.text = book.volumeInfo.description
        Glide.with(this)
            .load(book.volumeInfo.imageLinks?.thumbnail)
            .into(imageView)
    }
}
