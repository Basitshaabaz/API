package com.ili.digital.assessmentproject.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadUrl(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(this)
}