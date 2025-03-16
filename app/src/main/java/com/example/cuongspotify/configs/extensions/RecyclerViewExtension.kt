package com.example.cuongspotify.configs.extensions

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addScrollToEndListener(callback: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
            val listSize = recyclerView.adapter?.itemCount ?: 0
            if (listSize > 0 && linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == listSize - 1) {
                callback()
            }
        }
    })
}