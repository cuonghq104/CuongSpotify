package com.example.cuongspotify.views.screens.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cuongspotify.R
import com.example.cuongspotify.configs.extensions.addScrollToEndListener
import com.example.cuongspotify.databinding.FragmentHomeBinding
import com.example.cuongspotify.views.components.spacings.MarginItemDecoration
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModal by activityViewModels()

    private val listAdapter: HomePlaylistListAdapter = HomePlaylistListAdapter(HomePlaylistListAdapter.ModelType.CATEGORY, listOf())

    private val newReleaseAdapter: HomePlaylistListAdapter = HomePlaylistListAdapter(HomePlaylistListAdapter.ModelType.ALBUM, listOf())

    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupUI()
        addListener()
        return binding.root
    }

    private fun setupUI() {
        with(binding) {
            rvTopSong.adapter = listAdapter
            rvTopSong.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTopSong.addItemDecoration(MarginItemDecoration(requireContext(), requireContext().resources.getDimensionPixelSize(
                R.dimen.recyclerViewItemSpacing), LinearLayoutManager.HORIZONTAL))

            rvAlbumNewRelease.adapter = newReleaseAdapter
            rvAlbumNewRelease.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvAlbumNewRelease.addItemDecoration(MarginItemDecoration(requireContext(), requireContext().resources.getDimensionPixelSize(
                R.dimen.recyclerViewItemSpacing), LinearLayoutManager.HORIZONTAL))
        }
    }


    private fun initData() {
        viewModel.fetchMusicTrack(requireContext())
        viewModel.fetchNewReleaseAlbum(requireContext())
    }

    private fun addListener() {
        with(binding) {
            rvTopSong.addScrollToEndListener {
                if (!flag) {
                    flag = true
                    Log.d("TAG", "aaa")
                }
            }
        }

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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    albumNewRelease.collectLatest {
                        newReleaseAdapter.data = it
                        newReleaseAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        lifecycleScope.launch {
            with(viewModel) {
                error.debounce(300L).collect {
                    Log.d("TAG", it)
                }
            }
        }
    }
}