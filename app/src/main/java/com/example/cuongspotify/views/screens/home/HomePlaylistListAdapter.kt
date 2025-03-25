package com.example.cuongspotify.views.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil3.load
import com.example.cuongspotify.databinding.ItemHomeAlbumBinding
import com.example.cuongspotify.databinding.ItemHomeTopTrackBinding
import com.example.cuongspotify.databinding.ItemPlaylistRowBinding
import com.example.cuongspotify.models.BrowseCategory
import com.example.cuongspotify.models.HomeListItem

class HomePlaylistListAdapter(var type: ModelType, var data: List<HomeListItem>, var onItemClick: ((item: HomeListItem) -> Unit)?): RecyclerView.Adapter<HomePlaylistListAdapter.ViewHolder>() {

    enum class ModelType {
        CATEGORY,
        ALBUM,
        TRACK
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (type) {
            ModelType.CATEGORY -> ItemPlaylistRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ModelType.TRACK -> ItemHomeTopTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> ItemHomeAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = data[position]
        when (type) {
            ModelType.CATEGORY -> {
                with(holder.binding as ItemPlaylistRowBinding) {
                    tvTrackName.text = track.getTitle() ?: "Unknown track"
                    imvPlaylist.load(track.getImageUrl())
                }
            }

            ModelType.TRACK -> {
                with(holder.binding as ItemHomeTopTrackBinding) {
                    tvTitle.text = track.getTitle() ?: "Unknown album"
                    tvSubtitle.text = track.getSubtitle() ?: "Unknown artist"
                    imvTrack.load(track.getImageUrl())
                    clContainer.setOnClickListener {
                        onItemClick?.let {
                            it(track)
                        }
                    }
                }
            }

            ModelType.ALBUM -> {
                with(holder.binding as ItemHomeAlbumBinding) {
                    tvName.text = track.getTitle() ?: "Unknown album"
                    tvSubtitle.text = track.getSubtitle() ?: "Unknown artist"
                    imvPlaylist.load(track.getImageUrl())
                }
            }
        }
    }

    class ViewHolder(val binding: ViewBinding): RecyclerView.ViewHolder(binding!!.root) {

    }
}