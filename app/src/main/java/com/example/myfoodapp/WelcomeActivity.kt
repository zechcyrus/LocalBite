package com.example.myfoodapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myfoodapp.auth.LoginActivity
import com.example.myfoodapp.auth.RegisterActivity
import com.example.myfoodapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBuyFood.setOnClickListener {
            // Navigate to login for buyers
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra("user_type", "buyer")
                putExtra("from_welcome", true)
            })
        }

        binding.btnSellFood.setOnClickListener {
            // Navigate to login for cooks
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra("user_type", "cook")
                putExtra("from_welcome", true)
            })
        }
    }
}