package com.example.cuongspotify.views.screens.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cuongspotify.R
import com.example.cuongspotify.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        addListener()
        return binding.root
    }

    private fun addListener() {
        with(binding) {
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_spotifyAuthFragment)
            }
        }
    }
}