package com.example.utss

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController


/*import com.example.UTSS.databinding.ActivityMainBinding*/

/*
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_editprofil)
    }
}
*/


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val daftarTextView = findViewById<TextView>(R.id.daftartext)
        daftarTextView.setOnClickListener {
            // Gunakan NavController untuk melakukan navigasi ke FragmentDaftar
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_daftarFragment)

        }
        val masukButton = findViewById<Button>(R.id.masukbutton)
        masukButton.setOnClickListener {
            // Gunakan NavController untuk melakukan navigasi ke FragmentMenu
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_homeFragment)
        }

    }
}