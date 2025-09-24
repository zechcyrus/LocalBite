package com.example.myfoodapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myfoodapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository
    private var userType: String = "buyer"
    private var fromWelcome: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository()
        userType = intent.getStringExtra("user_type") ?: "buyer"
        fromWelcome = intent.getBooleanExtra("from_welcome", false)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            handleSendOtp()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java).apply {
                putExtra("user_type", userType)
                putExtra("from_welcome", fromWelcome)
            })
        }
    }

    private fun handleSendOtp() {
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val result = authRepository.sendEmailOtp(email, createUserIfNeeded = true)
            result.fold(
                onSuccess = {
                    Toast.makeText(this@LoginActivity, "OTP sent to $email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, VerifyOtpActivity::class.java).apply {
                        putExtra("email", email)
                    })
                },
                onFailure = { e ->
                    Toast.makeText(this@LoginActivity, "Failed to send OTP: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}