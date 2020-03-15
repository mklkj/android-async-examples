package io.github.mklkj.android_async_examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import io.github.mklkj.android_async_examples.fragments.AsyncTaskFragment
import io.github.mklkj.android_async_examples.fragments.JavaThreadFragment
import io.github.mklkj.android_async_examples.fragments.KotlinCoroutinesFragment
import io.github.mklkj.android_async_examples.fragments.RxJavaFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val fragments = listOf(
        "AsyncTask" to AsyncTaskFragment(),
        "Java Thread" to JavaThreadFragment(),
        "Kotlin Coroutines" to KotlinCoroutinesFragment(),
        "RxJava & RxAndroid" to RxJavaFragment()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(listView) {
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, fragments.map { it.first })
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                (activity as? MainActivity)?.run {
                    val fragment = fragments[position]
                    setTitle(fragment.first)
                    pushFragment(fragment.second)
                }
            }
        }
    }
}
