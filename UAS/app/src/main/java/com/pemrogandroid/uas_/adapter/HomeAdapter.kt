package com.pemrogandroid.uas_.adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pemrogandroid.uas_.Interfac.OnItemClickListener
=======

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
>>>>>>> 22bffd882a89048a4ebeca760d2104e6638c7b3a
import com.pemrogandroid.uas_.R
import com.pemrogandroid.uas_.model.BookItem
import com.squareup.picasso.Picasso

<<<<<<< HEAD
class HomeAdapter(private var books: List<BookItem>, private val itemClickListener: OnItemClickListener):  RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val titleTextView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val bookItem_ = books[position]
                itemClickListener.onItemClick(bookItem_)
            }
        }
    }


=======
class HomeAdapter(private var books: List<BookItem>):  RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }



>>>>>>> 22bffd882a89048a4ebeca760d2104e6638c7b3a
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




