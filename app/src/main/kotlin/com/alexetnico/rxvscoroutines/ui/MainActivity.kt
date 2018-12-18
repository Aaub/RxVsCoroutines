package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.alexetnico.rxvscoroutines.R
import com.alexetnico.rxvscoroutines.ui.MainViewModel.STATUS.LOADING
import com.alexetnico.rxvscoroutines.ui.MainViewModel.STATUS.NOT_LOADING
import com.alexetnico.rxvscoroutines.utils.toBeersString
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
        listenEditText()
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

        viewModel.beersCo.observe(this, Observer {
            it?.let { random_beers_co.text = it.toBeersString() }
        })
        viewModel.beersRx.observe(this, Observer {
            it?.let { random_beers_rx.text = it.toBeersString() }
        })

        viewModel.beersStatusCo.observe(this, Observer {
            beers_loader_co.visibility = when (it) {
                LOADING -> VISIBLE
                NOT_LOADING, null -> INVISIBLE
            }
        })
        viewModel.beersStatusRx.observe(this, Observer {
            beers_loader_rx.visibility = when (it) {
                LOADING -> VISIBLE
                NOT_LOADING, null -> INVISIBLE
            }
        })
    }

    private fun setupListeners() {
        random_btn.setOnClickListener { viewModel.fetchRandomBeer() }
        beer_with_image_btn.setOnClickListener { viewModel.fetchBeerImage() }
        random_beers_btn.setOnClickListener { viewModel.fetchRandomBeers() }
    }

    private fun listenEditText() {
        beer_quantity_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    MainViewModel.QUANTITY = s?.toString()?.toInt() ?: 0
                } catch (e: Exception) {
                    Log.e("MainActivity", "NaN")
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }


}
