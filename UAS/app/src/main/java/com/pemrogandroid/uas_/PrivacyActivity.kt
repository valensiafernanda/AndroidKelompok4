package com.pemrogandroid.uas_

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PrivacyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        val backButton: ImageView = findViewById(R.id.btn_back_)
        backButton.setOnClickListener {
            finish()
        }
    }
}
