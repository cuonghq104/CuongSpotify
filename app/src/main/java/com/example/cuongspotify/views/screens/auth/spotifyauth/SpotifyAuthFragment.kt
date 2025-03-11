package com.example.cuongspotify.views.screens.auth.spotifyauth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cuongspotify.R
import com.example.cuongspotify.configs.AppConstants
import com.example.cuongspotify.configs.AppConstants.getSpotifyLoginUrl
import com.example.cuongspotify.databinding.FragmentSpotifyAuthBinding
import com.example.cuongspotify.views.screens.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpotifyAuthFragment : Fragment() {

    private var _binding: FragmentSpotifyAuthBinding?= null
    val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    private var isWebviewDirectToCallback = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpotifyAuthBinding.inflate(inflater, container, false)
        addListener()
        setupUI()
        initData()
        return binding.root
    }

    private fun initData() {
        isWebviewDirectToCallback = false
    }

    private fun setupUI() {
        val loginUrl = getSpotifyLoginUrl()
        with(binding) {
            // Avoid open external browser
            wvSpotifyAuth.webViewClient = object: WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return false
                }

                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    url?.let {
                        if (it.startsWith("https://www.google.com/?code=") && !isWebviewDirectToCallback) {
                            isWebviewDirectToCallback = true
                            val code = it.replace("https://www.google.com/?code=", "")
                            viewModel.getAccessTokenFromCode(code, requireContext())
                        }
                    }
                    super.doUpdateVisitedHistory(view, url, isReload)
                }
            }

            wvSpotifyAuth.settings.apply {
                // Avoid spotify block when load login url from webview
                javaScriptEnabled = true
                domStorageEnabled = true
                userAgentString = System.getProperty("http.agent")
            }
            wvSpotifyAuth.loadUrl(loginUrl)
        }
    }

    private fun addListener() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        lifecycleScope.launch {
            with(viewModel) {
                loggedInResult.collect {
                    val token = withContext(Dispatchers.IO) {
                        val sp = requireContext().getSharedPreferences(AppConstants.SP_NAME, Context.MODE_PRIVATE)
                        return@withContext sp.getString(AppConstants.SP_KEY_ACCESS_TOKEN, "")
                    }
                    Log.d("TAG::token", token ?: "")
                }
            }
        }
    }
}