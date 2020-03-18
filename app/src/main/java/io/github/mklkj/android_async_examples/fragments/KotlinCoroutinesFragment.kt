package io.github.mklkj.android_async_examples.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import io.github.mklkj.android_async_examples.R
import kotlinx.android.synthetic.main.fragment_example.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.URL

@ExperimentalCoroutinesApi
class KotlinCoroutinesFragment : Fragment() {

    private var coroutine: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startAsyncTask()

        buttonRefresh.setOnClickListener {
            coroutine?.cancel(CancellationException("New coroutine started"))
            startAsyncTask()
        }
    }

    private fun startAsyncTask() {
        coroutine = viewLifecycleOwner.lifecycleScope.launch {
            flow { emit(makeRequestAndReturnResponse(URL("https://httpbin.org/anything/android-async-examples"))) }
                .onStart {
                    setMessage(getString(R.string.loading))
                    Log.i("CoroutinesFragment", "loading was started")
                }.catch {
                    setMessage(it.message)
                    Log.e("CoroutinesFragment", "loading error: ${it.message}", it)
                }.flowOn(Dispatchers.IO).collect {
                    textResult.text = it
                    Log.i("CoroutinesFragment", "loading completed")
                }
        }
    }

    private suspend fun setMessage(message: String?) {
        withContext(Dispatchers.Main) {
            textResult.text = message
        }
    }
}
