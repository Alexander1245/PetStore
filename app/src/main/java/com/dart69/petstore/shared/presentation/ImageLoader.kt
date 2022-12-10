package com.dart69.petstore.shared.presentation

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.dart69.petstore.R
import java.io.File
import java.util.*

interface ImageLoader {
    fun loadInto(imageUri: String, into: ImageView)

    fun download(imageUri: String)
}

abstract class DefaultLoader(
    context: Context
) : ImageLoader {
    protected val applicationContext: Context = context.applicationContext

    protected open fun createFileName(): String = IMAGE_PREFIX + Date().time

    protected open fun buildRequest(uri: Uri, fileName: String): Request = Request(uri)
        .setAllowedOverRoaming(false)
        .setAllowedNetworkTypes(Request.NETWORK_WIFI or Request.NETWORK_MOBILE)
        .setTitle(fileName)
        .setMimeType(MIME_TYPE)
        .setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(
            Environment.DIRECTORY_PICTURES,
            File.separator + fileName + EXTENSION
        )

    override fun download(imageUri: String) {
        val downloadManager =
            ContextCompat.getSystemService(applicationContext, DownloadManager::class.java)
        val request = buildRequest(imageUri.toUri(), createFileName())
        downloadManager?.enqueue(request)
    }

    protected companion object {
        const val IMAGE_PREFIX = "IMG_"
        const val MIME_TYPE = "image/jpeg"
        const val EXTENSION = ".jpg"
    }
}

class GlideImplementation(
    context: Context,
) : DefaultLoader(context) {
    override fun loadInto(imageUri: String, into: ImageView) {
        Glide.with(applicationContext)
            .load(imageUri)
            .error(R.drawable.ic_sync_problem)
            .centerCrop()
            .into(into)
    }
}