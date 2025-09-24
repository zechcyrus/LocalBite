package com.example.myfoodapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myfoodapp.comingsoon.ComingSoonActivity
import com.example.myfoodapp.databinding.ActivityVerifyOtpBinding
import kotlinx.coroutines.launch

class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyOtpBinding
    private lateinit var authRepository: AuthRepository

    private var email: String = ""
    private var name: String = ""
    private var password: String = ""
    private var phone: String = ""
    private var address: String = ""
    private var userType: String = ""
    private var fromWelcome: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository()
        
        // Get all registration data
        email = intent.getStringExtra("email") ?: ""
        name = intent.getStringExtra("name") ?: ""
        password = intent.getStringExtra("password") ?: ""
        phone = intent.getStringExtra("phone") ?: ""
        address = intent.getStringExtra("address") ?: ""
        userType = intent.getStringExtra("user_type") ?: "buyer"
        fromWelcome = intent.getBooleanExtra("from_welcome", false)

        binding.tvEmail.text = email
        
        // Show OTP sent message immediately
        Toast.makeText(this, "OTP sent to $email", Toast.LENGTH_SHORT).show()

        binding.btnVerify.setOnClickListener {
            val code = binding.etOtp.text.toString().trim()
            if (code.length < 4) {
                Toast.makeText(this, "Enter the OTP sent to your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val result = authRepository.verifyEmailOtp(email, code)
                result.fold(
                    onSuccess = {
                        startActivity(Intent(this@VerifyOtpActivity, ComingSoonActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                        finish()
                    },
                    onFailure = { e ->
                        Toast.makeText(this@VerifyOtpActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}


