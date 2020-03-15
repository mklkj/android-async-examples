package io.github.mklkj.android_async_examples.fragments

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

// just request example using HttpURLConnection. Use e.g. okHttp instead
fun makeRequestAndReturnResponse(url: URL): String {
    val output: String

    val connection = url.openConnection() as HttpURLConnection
    connection.setRequestProperty("Content-Type", "application/json")

    try {
        connection.doOutput = true
        connection.setChunkedStreamingMode(0)

        output = connection.inputStream.bufferedReader().use { it.readText() }

        Log.d("Utils", "response: ${connection.responseCode} ${connection.responseMessage}")
    } catch (e: Exception) {
        connection.disconnect()
        throw e
    }

    connection.disconnect()

    return output
}
