package com.pemrogandroid.uas_

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pemrogandroid.uas_.databinding.FragmentDaftarBinding

class DaftarActivity : AppCompatActivity() {
    private lateinit var binding: FragmentDaftarBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var passwordToggle: ImageView
    private lateinit var confirmPasswordToggle: ImageView
    private var isPasswordVisible: Boolean = false
    private var isConfirmPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = FragmentDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        passwordEditText = findViewById(R.id.PasswordMasuk)
        confirmPasswordEditText = findViewById(R.id.ConfirmPassword)
        passwordToggle = findViewById(R.id.password_toggle)
        confirmPasswordToggle = findViewById(R.id.password_toggle1)

        passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        confirmPasswordToggle.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

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

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24)
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passwordToggle.setImageResource(R.drawable.baseline_visibility_off_24)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            confirmPasswordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24)
        } else {
            confirmPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            confirmPasswordToggle.setImageResource(R.drawable.baseline_visibility_off_24)
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible
        confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
    }

}