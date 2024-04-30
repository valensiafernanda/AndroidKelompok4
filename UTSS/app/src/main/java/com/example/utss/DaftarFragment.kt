package com.example.utss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DaftarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DaftarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_daftar, container, false)

        // Temukan tombol daftar
        val daftarButton = view.findViewById<Button>(R.id.daftarbutton)

        // Tambahkan OnClickListener ke tombol daftar
        daftarButton.setOnClickListener {
            // Gunakan NavController untuk melakukan navigasi ke fragment tujuan
            findNavController().navigate(R.id.action_daftarFragment_to_homeFragment)
        }

        return view
    }
}
