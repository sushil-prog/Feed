package com.aiegroup.todo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.ulu.feed.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Utility {
    companion object {
        private var dfDDMMYYHHMM: DateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        /**
         * .check network connection function
         */
        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting
            return isConnected
        }
        @JvmStatic
        @BindingAdapter("load_image")
        fun loadImage(imageView: ShapeableImageView, thumbnail_image_url: String) {
            Glide.with(imageView.context).load(thumbnail_image_url).centerCrop().placeholder(R.drawable.error_thumbnail).error(R.drawable.error_thumbnail).into(imageView);

        }
        @JvmStatic
        @BindingAdapter("setDate")
        fun setDate(textView: TextView, dateInMilliSeconds: Long) {
            textView.text =ToDDMMMYYYYHHMM(Date(dateInMilliSeconds*1000))
        }
        @JvmStatic
        @BindingAdapter("visibleGone")
        fun showHide(view: View, show: Boolean) {
            view.visibility = if (show) View.VISIBLE else View.GONE
        }
        @JvmStatic
        fun ToDDMMMYYYYHHMM(value: Date): String {
            return if (value != null) {
                dfDDMMYYHHMM.format(value)
            } else ({
                null
            }).toString()
        }
    }
}