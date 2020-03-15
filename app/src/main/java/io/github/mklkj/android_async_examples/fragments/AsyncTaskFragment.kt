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
import java.io.IOException
import java.net.URL

class AsyncTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            return try {
                makeRequestAndReturnResponse(params[0])
            } catch (e: IOException) {
                Log.e("AsyncTaskFragment", e.message, e)
                e.message.orEmpty()
            }
        }

        override fun onPostExecute(result: String) {
            textResult.text = result
        }
    }
}
