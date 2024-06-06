package com.pemrogandroid.uas_.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pemrogandroid.uas_.EditProfileActivity
import com.pemrogandroid.uas_.LoginActivity
import com.pemrogandroid.uas_.PrivacyActivity
import com.pemrogandroid.uas_.databinding.FragmentProfilBinding

class ProfileFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val profilViewModel = ViewModelProvider(this).get(ProfilViewModel::class.java)

        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textProfile
        profilViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = user.email
            binding.email.text = email
        }

        firestore = FirebaseFirestore.getInstance()

        user?.uid?.let { uid ->
            firestore.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.getString("username")
                    binding.textView3.text = username
                } else {
                    Toast.makeText(requireContext(), "No profile data found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageView5.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.privacybutton.setOnClickListener {
            val intent = Intent(activity, PrivacyActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
        loadProfileImage()
    }

    private fun loadProfileData() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.uid?.let { uid ->
            firestore.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.getString("username")
                    binding.textView3.text = username
                    val email = document.getString("email")
                    binding.email.text = email
                } else {
                    Toast.makeText(requireContext(), "No profile data found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProfileImage() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = user.uid
            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val imageUrl = document.getString("imageUrl")
                        if (!imageUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(imageUrl)
                                .into(binding.imageView6) // Replace with your ImageView ID
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load profile image", Toast.LENGTH_SHORT).show()
                }
        }
    }
}