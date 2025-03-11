package com.example.cuongspotify.views.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cuongspotify.R
import com.example.cuongspotify.databinding.FragmentHomeBinding
import com.example.cuongspotify.views.components.spacings.MarginItemDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModal by activityViewModels()

    private val listAdapter: HomePlaylistListAdapter = HomePlaylistListAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        addListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        with(binding) {
            rvTopSong.adapter = listAdapter
            rvTopSong.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTopSong.addItemDecoration(MarginItemDecoration(requireContext(), requireContext().resources.getDimensionPixelSize(
                R.dimen.recyclerViewItemSpacing), LinearLayoutManager.HORIZONTAL))
        }
    }


    private fun initData() {
        viewModel.fetchMusicTrack(requireContext())
    }

    private fun addListener() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    musicTrack.collectLatest {
                        listAdapter.data = it
                        listAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}