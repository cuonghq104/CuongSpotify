package com.example.cuongspotify.views.components.spacings

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.compose.ui.res.dimensionResource
import androidx.recyclerview.widget.RecyclerView
import com.example.cuongspotify.R

class MarginItemDecoration(private val context: Context, private val spaceSize: Int, private val orientation: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (orientation == LinearLayout.HORIZONTAL) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    left = context.resources.getDimensionPixelSize(R.dimen.screenPadding)
                } else {
                    left = spaceSize
                }

                if (parent.getChildAdapterPosition(view) == state.itemCount) {
                    right = context.resources.getDimensionPixelSize(R.dimen.screenPadding)
                } else {
                    right = spaceSize
                }
            } else {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = context.resources.getDimensionPixelSize(R.dimen.screenPadding)
                } else {
                    top = spaceSize
                }

                if (parent.getChildAdapterPosition(view) == state.itemCount) {
                    bottom = context.resources.getDimensionPixelSize(R.dimen.screenPadding)
                } else {
                    bottom = spaceSize
                }
            }
        }
    }
}