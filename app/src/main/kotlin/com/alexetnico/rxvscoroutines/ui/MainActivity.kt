package com.alexetnico.rxvscoroutines.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alexetnico.rxvscoroutines.R
import com.alexetnico.rxvscoroutines.utils.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        viewModel { MainViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
    }

    private fun observeViewModel() {

    }
}
