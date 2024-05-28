package com.pemrogandroid.uas_.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pemrogandroid.uas_.SearchResultActivity
import com.pemrogandroid.uas_.databinding.FragmentSearchBinding



class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.search2.setOnClickListener {
            val query = binding.searchinput.text.toString()
            if (query.isNotEmpty()) {
                val intent = Intent(context, SearchResultActivity::class.java)
                intent.putExtra("QUERY", query)
                startActivity(intent)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}