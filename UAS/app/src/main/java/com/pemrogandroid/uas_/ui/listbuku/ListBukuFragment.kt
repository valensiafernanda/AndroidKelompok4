package com.pemrogandroid.uas_.ui.listbuku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pemrogandroid.uas_.R
import com.pemrogandroid.uas_.adapter.ListBukuAdapter
import com.pemrogandroid.uas_.model.UserBook
import androidx.appcompat.app.AlertDialog

@Suppress("DEPRECATION")
class ListBukuFragment : Fragment() {

    private lateinit var listbukuview: ListBukuViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListBukuAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var borrowedBooks: MutableList<UserBook> = mutableListOf()
    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listbukuview = ViewModelProvider(this).get(ListBukuViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list_buku, container, false)

        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ListBukuAdapter(borrowedBooks) { book -> showDeleteConfirmationDialog(book) }
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        currentUserId?.let {
            reference = database.reference.child("user_books").child(it)
            loadBorrowedBooks()
        }
        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showDeleteConfirmationDialog(book: UserBook) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogbox, null)

        dialogView.findViewById<Button>(R.id.dialog_ok).setOnClickListener {
            deleteBook(book)
            alertDialog?.dismiss()
        }

        dialogView.findViewById<Button>(R.id.dialog_cancel).setOnClickListener {
            alertDialog?.dismiss()
        }

        builder.setView(dialogView)
        alertDialog = builder.create()
        alertDialog?.show()
    }


    private fun deleteBook(book: UserBook) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        currentUserId?.let {
            reference.child(book.bookId).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val bookRef = database.reference.child("books").child(book.bookId)
                    bookRef.child("availableQuantity").runTransaction(object : Transaction.Handler {
                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
                            val currentQuantity = mutableData.getValue(Int::class.java) ?: return Transaction.success(mutableData)
                            val borrowedQuantity = book.jumlahBuku
                            mutableData.value = currentQuantity + borrowedQuantity
                            return Transaction.success(mutableData)
                        }

                        override fun onComplete(databaseError: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                            if (databaseError != null) {

                            }
                        }
                    })
                }
            }
        }
    }



    private fun loadBorrowedBooks() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                borrowedBooks.clear()
                for (snapshot in dataSnapshot.children) {
                    val userBook = snapshot.getValue(UserBook::class.java)
                    userBook?.let { borrowedBooks.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
