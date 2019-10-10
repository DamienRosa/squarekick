package com.dgr.squarekick.utils

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import com.dgr.squarekick.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class ImageViewURL : ImageView {

    var url: String = ""
        set(value) {
            field = value
            requestGlide(value, this)
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ImageViewURL)
        val url = array.getString(R.styleable.ImageViewURL_url)
        if (!url.isNullOrEmpty()) {
            requestGlide(url, this)
        }
        array.recycle()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun requestGlide(url: String, imageView: ImageView) {
        GlideToVectorYou
            .init()
            .with(imageView.context.applicationContext)
            .load(Uri.parse(url), imageView)
    }
}