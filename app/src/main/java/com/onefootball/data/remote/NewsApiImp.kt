package com.onefootball.data.remote

import android.content.Context
import java.nio.charset.Charset

class NewsApiImp(private val context: Context): NewsApi {

    override fun readFile(name: String): String {
        val inputStream = context.assets.open(name)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        return buffer.toString(Charset.defaultCharset())
    }
}