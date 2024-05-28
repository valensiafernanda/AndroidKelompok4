package com.pemrogandroid.uas_.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pemrogandroid.uas_.R
import com.pemrogandroid.uas_.ui.home.HomeViewModel

class SearchFragment : Fragment() {

    private lateinit var searchviewmodel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchviewmodel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        searchviewmodel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
