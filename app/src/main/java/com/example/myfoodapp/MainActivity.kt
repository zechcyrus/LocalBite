package com.example.myfoodapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myfoodapp.auth.LoginActivity
import com.example.myfoodapp.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        // Initially hide the Veg/NonVeg toggle (will only show on Home page)
        binding.toggleGroup.visibility = View.GONE
        
        // Set initial selection to Veg
        binding.toggleGroup.check(binding.btnVeg.id)
        
        // Set up Veg/NonVeg toggle
        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.btnVeg.id -> {
                        // Handle Veg selection
                        // TODO: Load veg items
                    }
                    binding.btnNonVeg.id -> {
                        // Handle NonVeg selection
                        // TODO: Load non-veg items
                    }
                }
            }
        }
        
        // Set up bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Show Veg/NonVeg toggle on Home page
                    binding.toggleGroup.visibility = View.VISIBLE
                    // Load home content
                    loadHomeContent()
                    true
                }
                R.id.nav_cart -> {
                    // Hide Veg/NonVeg toggle on other pages
                    binding.toggleGroup.visibility = View.GONE
                    // Load cart content
                    loadCartContent()
                    true
                }
                R.id.nav_map -> {
                    // Hide Veg/NonVeg toggle on other pages
                    binding.toggleGroup.visibility = View.GONE
                    // Load map content
                    loadMapContent()
                    true
                }
                R.id.nav_history -> {
                    // Hide Veg/NonVeg toggle on other pages
                    binding.toggleGroup.visibility = View.GONE
                    // Load history content
                    loadHistoryContent()
                    true
                }
                R.id.nav_profile -> {
                    // Hide Veg/NonVeg toggle on other pages
                    binding.toggleGroup.visibility = View.GONE
                    // Load profile content with logout button
                    loadProfileContent()
                    true
                }
                else -> false
            }
        }
        
        // Set default selected item to Home
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }
    
    private fun loadHomeContent() {
        // Clear any existing content
        binding.contentContainer.removeAllViews()
        // TODO: Load actual home content
    }
    
    private fun loadCartContent() {
        // Clear any existing content
        binding.contentContainer.removeAllViews()
        // TODO: Load actual cart content
    }
    
    private fun loadMapContent() {
        // Clear any existing content
        binding.contentContainer.removeAllViews()
        // TODO: Load actual map content
    }
    
    private fun loadHistoryContent() {
        // Clear any existing content
        binding.contentContainer.removeAllViews()
        // TODO: Load actual history content
    }
    
    private fun loadProfileContent() {
        // Clear any existing content
        binding.contentContainer.removeAllViews()
        
        // Inflate the profile view with logout button
        val profileView = layoutInflater.inflate(R.layout.fragment_profile, binding.contentContainer, false)
        val logoutButton = profileView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogout)
        
        logoutButton.setOnClickListener {
            // Handle logout - navigate back to login screen
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        
        binding.contentContainer.addView(profileView)
    }
}