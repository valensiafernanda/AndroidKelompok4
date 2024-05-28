package com.pemrogandroid.uas_.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pemrogandroid.uas_.R
import com.pemrogandroid.uas_.model.BookItem
import com.squareup.picasso.Picasso

class SearchResultAdapter(private var bookList: List<BookItem>) : RecyclerView.Adapter<SearchResultAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.mBookTitle)
        val authorName: TextView = itemView.findViewById(R.id.mAuthorName)
        val bookImage: ImageView = itemView.findViewById(R.id.mBookImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position].volumeInfo
        holder.bookTitle.text = book.title
        val authorsString = book.authors?.joinToString(", ") ?: "Penulis tidak diketahui"
        holder.authorName.text = authorsString


        book.imageLinks?.thumbnail?.let {
            Picasso.get().load(it).into(holder.bookImage)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun updateData(newBookList: List<BookItem>) {
        bookList = newBookList
        notifyDataSetChanged()
    }
}
