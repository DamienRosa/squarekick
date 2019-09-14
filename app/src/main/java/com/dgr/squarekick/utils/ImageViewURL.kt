package com.dgr.squarekick.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.dgr.squarekick.R

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
        if (url != null) {
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
//        val httpClient: OkHttpClient = OkHttpClient.Builder()
//            .cache(Cache(context.cacheDir, 5 * 1024 * 1014))
//            .build()
//
//        val request = Request.Builder().url(url).build()
//        httpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val stream: InputStream? = response.body()?.byteStream()
//                Sharp.loadInputStream(stream).into(imageView)
//            }
//        })
    }
}