package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.alexetnico.rxvscoroutines.R
import com.alexetnico.rxvscoroutines.utils.toast
import com.alexetnico.rxvscoroutines.utils.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        viewModel { MainViewModel(getString(R.string.brewery_api_key)) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.beers.observe(this, Observer {
            it?.let {
                Log.d("BEER", "$it")
            }
        })

        viewModel.beersRx.observe(this, Observer {
            it?.let {
                toast(it.first().name)
            }
        })
    }
}
