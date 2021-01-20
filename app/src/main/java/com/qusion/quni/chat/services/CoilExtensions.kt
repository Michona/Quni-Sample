package com.qusion.quni.chat.services

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.Disposable
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation

internal fun Context.loadBitmap(url: String, block: (Bitmap) -> Unit): Disposable {
    val req = ImageRequest.Builder(this)
        .data(url)
        .transformations(CircleCropTransformation())
        .target {
            // Image loaded
            block.invoke(it.toBitmap())
        }
        .build()

    return Coil.imageLoader(this).enqueue(req)
}
