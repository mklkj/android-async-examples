package io.github.mklkj.android_async_examples.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.mklkj.android_async_examples.R
import kotlinx.android.synthetic.main.fragment_example.*
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startAsyncTask()

        buttonRefresh.setOnClickListener {
            startAsyncTask()
        }
    }

    private fun startAsyncTask() {
        OurAsyncTask().execute(URL("https://httpbin.org/anything/android-async-examples"))
    }

    inner class OurAsyncTask : AsyncTask<URL, Int, String>() {

        override fun onPreExecute() {
            textResult.text = getString(R.string.loading)
        }

        override fun doInBackground(vararg params: URL): String {
            return makeRequestAndReturnResponse(params[0])
        }

        override fun onPostExecute(result: String) {
            textResult.text = result
        }

        // just request example using HttpURLConnection. Use e.g. okHttp instead
        private fun makeRequestAndReturnResponse(url: URL): String {
            var output = ""

            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Content-Type", "application/json")

            try {
                connection.doOutput = true
                connection.setChunkedStreamingMode(0)

                output = connection.inputStream.bufferedReader().use { it.readText() }

                Log.d("AsyncTaskFragment", "doInBackground: ${connection.responseCode} ${connection.responseMessage}")
            } catch (e: Exception) {
                output = e.message.orEmpty()
                Log.e("AsyncTaskFragment", e.message, e)
            } finally {
                connection.disconnect()
            }

            return output
        }
    }
}
