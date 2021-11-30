package com.ananth.actorsearch

import android.graphics.Bitmap
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Networking {
    val client: OkHttpClient = OkHttpClient()
    val baseUrl = "http://10.0.2.2:5000"

    // this is synchonous, only call withing coroutine
    fun postImage(image:File):String {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image","image.png", image.asRequestBody("image/png".toMediaTypeOrNull()))
            .build()
        val request = Request.Builder()
            .url("$baseUrl/search")
            .post(requestBody)
            .build()
        client.newCall(request).execute().use { response -> return response.body!!.string() }

    }
}