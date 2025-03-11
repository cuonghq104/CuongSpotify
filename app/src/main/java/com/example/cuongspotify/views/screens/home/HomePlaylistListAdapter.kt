package com.example.cuongspotify.views.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.cuongspotify.databinding.ItemPlaylistRowBinding
import com.example.cuongspotify.models.BrowseCategory
import com.example.cuongspotify.models.MusicTrack

class HomePlaylistListAdapter(var data: List<BrowseCategory>): RecyclerView.Adapter<HomePlaylistListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaylistRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = data[position]
        with(holder.binding) {
            tvTrackName.text = track.name ?: "Unknown track"
            imvPlaylist.load(track.icons[0].url)
        }
    }

    class ViewHolder(val binding: ItemPlaylistRowBinding): RecyclerView.ViewHolder(binding!!.root) {

    }
}