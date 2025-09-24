package com.example.myfoodapp.comingsoon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myfoodapp.databinding.ActivityComingSoonBinding

class ComingSoonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComingSoonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComingSoonBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}


