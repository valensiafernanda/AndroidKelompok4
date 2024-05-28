package com.pemrogandroid.uas_.ui.listbuku

import com.pemrogandroid.uas_.ui.profile.ProfilViewModel



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pemrogandroid.uas_.R

class ListBukuFragment : Fragment() {

    private lateinit var listbukuview: ListBukuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listbukuview = ViewModelProvider(this).get(ListBukuViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list_buku, container, false)
        // You can optionally update UI elements here using profileViewModel
        return root
    }
}
