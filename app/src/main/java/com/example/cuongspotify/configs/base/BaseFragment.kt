package com.example.cuongspotify.configs.base

import android.view.View
import androidx.fragment.app.Fragment
import com.example.cuongspotify.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    abstract fun getParentView(): View

    fun showMessageSnackBar(msg: String) {
        val snackBar = Snackbar.make(
            getParentView(),
            msg,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction(R.string.close) {
            snackBar.dismiss()
        }
        snackBar.show()
    }
}