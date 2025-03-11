package com.example.cuongspotify.views.screens.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cuongspotify.R
import com.example.cuongspotify.configs.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : AppCompatActivity() {
    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addListener()
    }

    private fun addListener() {
        lifecycleScope.launch {
            with(authViewModel) {
                loggedInResult.collect {
                    val backIntent = Intent()
                    backIntent.putExtra("action", "refresh")
                    setResult(RESULT_OK, backIntent)
                    finish()
                }
            }
        }

    }

    companion object {
        fun getInstance(context: Context): Intent {
            val intent = Intent(context, AuthActivity::class.java)
            return intent
        }
    }
}