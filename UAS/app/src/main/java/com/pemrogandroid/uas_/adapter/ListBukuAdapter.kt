package com.pemrogandroid.uas_.adapter

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pemrogandroid.uas_.R
import com.pemrogandroid.uas_.model.UserBook
import java.text.SimpleDateFormat
import java.util.*

class ListBukuAdapter(
    private val borrowedBooks: List<UserBook>,
    private val onDeleteClick: (UserBook) -> Unit
) : RecyclerView.Adapter<ListBukuAdapter.ListBukuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBukuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_buku, parent, false)
        return ListBukuViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ListBukuViewHolder, position: Int) {
        val borrowedBook = borrowedBooks[position]
        holder.bind(borrowedBook)
    }

    override fun getItemCount(): Int {
        return borrowedBooks.size
    }

    class ListBukuViewHolder(itemView: View, private val onDeleteClick: (UserBook) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val bookImage: ImageView = itemView.findViewById(R.id.mBookImage)
        private val bookTitle: TextView = itemView.findViewById(R.id.mBookTitle)
        private val bookCount: TextView = itemView.findViewById(R.id.angkajlh)
        private val borrowedAt: TextView = itemView.findViewById(R.id.tangalwaktupinjam_)
        private val returnAt: TextView = itemView.findViewById(R.id.waktukembali_)
        private val isConfirmed: TextView = itemView.findViewById(R.id.konfirmasi_)
        private val button_: ImageView = itemView.findViewById(R.id.mMenus)

        fun bind(borrowedBook: UserBook) {
            bookTitle.text = borrowedBook.title
            bookCount.text = " ${borrowedBook.jumlahBuku}"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            borrowedAt.text = " ${dateFormat.format(Date(borrowedBook.borrowedAt))}"

            if (borrowedBook.isConfirmed == true) {
                val borrowedDate = Date(borrowedBook.borrowedAt)
                val returnDate = Calendar.getInstance()
                returnDate.time = borrowedDate
                returnDate.add(Calendar.DAY_OF_YEAR, 7)
                returnAt.text = dateFormat.format(returnDate.time)
            } else {
                returnAt.text = "-"
            }

            isConfirmed.text = when {
                borrowedBook.isConfirmed == true -> "Silahkan Ambil"
                borrowedBook.isConfirmed == false -> "Tidak Ada"
                else -> "Diproses"
            }

            Glide.with(itemView.context).load(borrowedBook.imageUrl).into(bookImage)

            button_.setOnClickListener { view ->
                showPopupMenu(view, borrowedBook)
            }
        }

        private fun showPopupMenu(view: View, borrowedBook: UserBook) {
            val popupMenu = PopupMenu(view.context, view)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.show_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        onDeleteClick(borrowedBook)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}
