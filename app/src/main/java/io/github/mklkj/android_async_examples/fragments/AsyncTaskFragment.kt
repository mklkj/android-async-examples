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
import java.lang.Thread.sleep
import java.lang.ref.WeakReference
import java.net.URL

class AsyncTaskFragment : Fragment() {

    private var asyncTask: OurAsyncTask? = null

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
        asyncTask?.cancel(true)
        asyncTask = OurAsyncTask(this).apply {
            execute(URL("https://httpbin.org/anything/android-async-examples"))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        asyncTask?.cancel(true)
    }

    private class OurAsyncTask internal constructor(fragment: AsyncTaskFragment) : AsyncTask<URL, Int, String>() {

        private val fragmentRef: WeakReference<AsyncTaskFragment> = WeakReference(fragment)

        override fun onPreExecute() {
            fragmentRef.get()?.textResult?.text = fragmentRef.get()?.getString(R.string.loading)
            Log.i("AsyncTaskFragment", "OurAsyncTask was started")
        }

        override fun doInBackground(vararg params: URL): String {
            return try {
                sleep(2000)
                makeRequestAndReturnResponse(params[0])
            } catch (e: IOException) {
                Log.e("AsyncTaskFragment", "OurAsyncTask error: ${e.message}", e)
                e.message.orEmpty()
            }
        }

        override fun onPostExecute(result: String) {
            fragmentRef.get()?.textResult?.text = result
            Log.i("AsyncTaskFragment", "OurAsyncTask was completed")
        }

        override fun onCancelled() {
            Log.e("AsyncTaskFragment", "OurAsyncTask was cancelled")
        }

        override fun onCancelled(result: String?) {
            Log.e("AsyncTaskFragment", "OurAsyncTask was cancelled with result: $result")
        }
    }
}
