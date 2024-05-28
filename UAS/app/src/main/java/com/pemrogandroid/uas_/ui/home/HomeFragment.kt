package com.pemrogandroid.uas_.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.pemrogandroid.uas_.DetailActivity
import com.pemrogandroid.uas_.Interfac.OnItemClickListener
import com.pemrogandroid.uas_.adapter.HomeAdapter
import com.pemrogandroid.uas_.api.API
import com.pemrogandroid.uas_.databinding.FragmentHomeBinding
import com.pemrogandroid.uas_.model.BookItem
import com.pemrogandroid.uas_.model.BooksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private lateinit var api: API

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        api = retrofit.create(API::class.java)


        adapter = HomeAdapter(emptyList(),this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        fetchDataFromApi()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchDataFromApi() {

        GlobalScope.launch(Dispatchers.Main) {
            val response = api.searchBooks("fiction", 40,"AIzaSyCGd0ETMvaPt2cvinLhm8_Cf44_Tkj6gNM")
            if (response.isSuccessful) {
                val booksResponse: BooksResponse? = response.body()
                booksResponse?.items?.let {

                    adapter.updateData(it)
                }
            } else {
                //
            }
        }
    }

    override fun onItemClick(bookItem: BookItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("BOOK_ID", bookItem.id)
        }
        startActivity(intent)
    }
}
