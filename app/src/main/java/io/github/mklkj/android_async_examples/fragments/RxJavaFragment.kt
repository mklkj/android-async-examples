package io.github.mklkj.android_async_examples.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.mklkj.android_async_examples.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_example.*
import java.net.URL

class RxJavaFragment : Fragment() {

    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startAsyncTask()

        buttonRefresh.setOnClickListener {
            disposable.clear()
            startAsyncTask()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

    private fun startAsyncTask() {
        disposable.add(Single.fromCallable { makeRequestAndReturnResponse(URL("https://httpbin.org/anything/android-async-examples")) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                textResult.text = getString(R.string.loading)
                Log.i("RxJavaFragment", "loading was started")
            }
            .subscribe({
                textResult.text = it
                Log.i("RxJavaFragment", "loading completed")
            }, {
                textResult.text = it.message
                Log.e("RxJavaFragment", "loading error: ${it.message}", it)
            }))
    }
}
