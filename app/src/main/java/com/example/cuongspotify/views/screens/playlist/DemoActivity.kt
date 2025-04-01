package com.example.cuongspotify.views.screens.playlist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuongspotify.R
import com.example.cuongspotify.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {

    var _binding: ActivityDemoBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnARN.setOnClickListener {
            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < 10000) {
                // Vòng lặp này chặn UI trong 10 giây
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}