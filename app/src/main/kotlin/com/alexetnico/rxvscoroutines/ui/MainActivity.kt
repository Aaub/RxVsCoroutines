package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alexetnico.rxvscoroutines.R
import com.alexetnico.rxvscoroutines.utils.viewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        viewModel { MainViewModel(getString(R.string.brewery_api_key)) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {

        viewModel.beerCo.observe(this, Observer {
            it?.let { random_beer_co.setupView(it) }
        })

        viewModel.beerRx.observe(this, Observer {
            it?.let { random_beer_rx.setupView(it) }
        })

        viewModel.beerImageCo.observe(this, Observer {
            it?.let { beer_with_image_co.setupView(it) }
        })

        viewModel.beerImageRx.observe(this, Observer {
            it?.let { beer_with_image_rx.setupView(it) }
        })
    }

    private fun setupListeners() {
        random_button.setOnClickListener { viewModel.fetchRandomBeer() }
        beer_with_image_button.setOnClickListener { viewModel.fetchBeerImage() }

    }


}
