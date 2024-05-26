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

class HomeAdapter(private var books: List<BookItem>):  RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = books[position].volumeInfo
        holder.titleTextView.text = currentItem.title
        currentItem.imageLinks?.thumbnail?.let {
            Picasso.get().load(it).into(holder.imageView)
        }
    }

    override fun getItemCount() = books.size

    fun updateData(newBooks: List<BookItem>) {
        books = newBooks
        notifyDataSetChanged()
    }
}




