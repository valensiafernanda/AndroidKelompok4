package com.pemrogandroid.uas_

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pemrogandroid.uas_.databinding.EditprofileBinding


@Suppress("DEPRECATION")
class EditProfileActivity :AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: EditprofileBinding
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.changeProfileBtn.setOnClickListener {
            openFileChooser()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.Savebutton.setOnClickListener {
            val newPassword = binding.PasswordMasuk.text.toString()
            val newUsername = binding.fullnameeditprofil.text.toString()

            val currentUser = firebaseAuth.currentUser
            currentUser?.let { user ->
                val uid = user.uid

                val updateTasks = mutableListOf<Task<Void>>()


                if (newPassword.isNotEmpty()) {
                    val passwordUpdateTask = user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    updateTasks.add(passwordUpdateTask)
                }

                if (newUsername.isNotEmpty()) {
                    val usernameUpdateTask = firestore.collection("users").document(uid)
                        .update("username", newUsername)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Username updated", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update username", Toast.LENGTH_SHORT).show()
                        }
                    updateTasks.add(usernameUpdateTask)
                }

                if (imageUri != null) {
                    uploadImageToFirebase(imageUri!!) { imageUrl ->
                        firestore.collection("users").document(uid)
                            .update("imageUrl", imageUrl)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to update profile image URL in Firestore", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else if (updateTasks.isNotEmpty()) {
                    Tasks.whenAllComplete(updateTasks).addOnCompleteListener {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "No changes to update", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }


        binding.cancelBtn.setOnClickListener {
            finish()
        }

    }


    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.profileImageCircle.setImageURI(imageUri)
        }
    }

    private fun uploadImageToFirebase(uri: Uri, onSuccess: (String) -> Unit) {
        val storageRef = storage.reference.child("profile_images/${System.currentTimeMillis()}.jpg")
        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onSuccess(downloadUri.toString())
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }
}