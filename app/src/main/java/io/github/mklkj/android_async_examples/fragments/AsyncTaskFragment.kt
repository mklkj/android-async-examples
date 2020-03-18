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
import java.lang.ref.WeakReference
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
        OurAsyncTask(this).execute(URL("https://httpbin.org/anything/android-async-examples"))
    }

    private class OurAsyncTask internal constructor(fragment: AsyncTaskFragment) :
        AsyncTask<URL, Int, String>() {

        private val fragmentRef: WeakReference<AsyncTaskFragment> = WeakReference(fragment)

        override fun onPreExecute() {
            fragmentRef.get()?.textResult?.text = fragmentRef.get()?.getString(R.string.loading)
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
            fragmentRef.get()?.textResult?.text = result
        }
    }
}
