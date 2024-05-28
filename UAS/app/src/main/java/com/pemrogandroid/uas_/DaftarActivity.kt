package com.pemrogandroid.uas_

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pemrogandroid.uas_.databinding.FragmentDaftarBinding

class DaftarActivity : AppCompatActivity() {
    private lateinit var binding: FragmentDaftarBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = FragmentDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.daftar.setOnClickListener{
            val username = binding.editTextText.text.toString()
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.PasswordMasuk.text.toString()
            val rePassword = binding.ConfirmPassword.text.toString()
            if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty() ){
                if (password == rePassword ){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            val userId = it.result.user!!.uid
                            val user = hashMapOf(
                                "username" to username,
                                "email" to email
                            )

                            firestore.collection("users").document(userId)
                                .set(user)
                            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)

//
                        }else{
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password does not matched", Toast.LENGTH_SHORT).show()

                }
            }else{
                Toast.makeText(this,"Fields can not be empty ", Toast.LENGTH_SHORT).show()

            }


        }
        binding.masuk1.setOnClickListener {
            val signUpIntent = Intent(this,LoginActivity::class.java)
            startActivity(signUpIntent)
        }



    }


}