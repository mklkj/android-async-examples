package io.github.mklkj.android_async_examples.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.mklkj.android_async_examples.R
import kotlinx.android.synthetic.main.fragment_example.*
import java.lang.ref.WeakReference
import java.net.URL

class JavaThreadFragment : Fragment() {

    private var thread: Thread? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startAsyncTask()

        buttonRefresh.setOnClickListener {
            thread?.interrupt()
            startAsyncTask()
        }
    }

    private fun startAsyncTask() {
        thread = Thread(OurRunnable(this, URL("https://httpbin.org/anything/android-async-examples")))
        thread?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        thread?.interrupt()
    }

    private class OurRunnable(fragment: JavaThreadFragment, private val url: URL) : Runnable {

        private val fragmentRef: WeakReference<JavaThreadFragment> = WeakReference(fragment)

        override fun run() {
            onPreExecute()

            val message = doInBackground()

            onPostExecute(message)
        }

        private fun onPreExecute() {
            updateMessage(fragmentRef.get()?.getString(R.string.loading))
            Log.i("JavaThreadFragment", "OurRunnable was started")
        }

        private fun doInBackground(): String {
            return try {
                makeRequestAndReturnResponse(url)
            } catch (e: Throwable) {
                Log.e("JavaThreadFragment", "OurRunnable error: ${e.message}", e)
                e.message.orEmpty()
            }
        }

        private fun onPostExecute(message: String?) {
            updateMessage(message)
            Log.i("JavaThreadFragment", "OurRunnable was completed")
        }

        private fun updateMessage(message: String?) {
            fragmentRef.get()?.textResult?.run {
                post { text = message }
            }
        }
    }
}
