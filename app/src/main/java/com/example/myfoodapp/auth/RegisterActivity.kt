package com.example.myfoodapp.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myfoodapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepository: AuthRepository
    private var userType: String = "buyer"
    private var fromWelcome: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository()
        userType = intent.getStringExtra("user_type") ?: "buyer"
        fromWelcome = intent.getBooleanExtra("from_welcome", false)

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        val cameFromWelcome = fromWelcome
        
        // Hide role selection if user came from Welcome page (role already chosen)
        if (cameFromWelcome) {
            binding.tvSelectUserType.visibility = View.GONE
            binding.radioGroupUserType.visibility = View.GONE
        } else {
            binding.tvSelectUserType.visibility = View.VISIBLE
            binding.radioGroupUserType.visibility = View.VISIBLE
        }
        
        // Set initial user type selection
        if (userType == "cook") {
            binding.radioCook.isChecked = true
            binding.layoutAddress.visibility = View.VISIBLE
        } else {
            binding.radioBuyer.isChecked = true
            binding.layoutAddress.visibility = View.GONE
        }

        // Handle radio button changes (only if visible)
        if (!cameFromWelcome) {
            binding.radioGroupUserType.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    binding.radioBuyer.id -> {
                        userType = "buyer"
                        binding.layoutAddress.visibility = View.GONE
                    }
                    binding.radioCook.id -> {
                        userType = "cook"
                        binding.layoutAddress.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            handleSendOtp()
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra("user_type", userType)
                putExtra("from_welcome", fromWelcome)
            })
            finish()
        }
    }

    private fun handleSendOtp() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (userType == "cook" && address.isEmpty()) {
            Toast.makeText(this, "Address is required for cooks", Toast.LENGTH_SHORT).show()
            return
        }

        // Navigate to OTP page immediately
        startActivity(Intent(this, VerifyOtpActivity::class.java).apply {
            putExtra("email", email)
            putExtra("name", name)
            putExtra("password", password)
            putExtra("phone", phone)
            putExtra("address", address)
            putExtra("user_type", userType)
            putExtra("from_welcome", fromWelcome)
        })
        
        // Send OTP in background
        lifecycleScope.launch {
            authRepository.sendEmailOtp(email, createUserIfNeeded = true)
        }
    }
}