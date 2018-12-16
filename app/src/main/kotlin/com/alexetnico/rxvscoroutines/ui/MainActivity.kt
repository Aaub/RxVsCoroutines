package com.alexetnico.rxvscoroutines.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alexetnico.rxvscoroutines.R
import com.alexetnico.rxvscoroutines.model.Beer
import com.alexetnico.rxvscoroutines.ui.customview.BeerCard
import com.alexetnico.rxvscoroutines.utils.viewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        viewModel { MainViewModel(getString(R.string.brewery_api_key)) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        random_button.setOnClickListener { onClickRandomBeer() }
    }

    private fun onClickRandomBeer() {

        viewModel.beerCo.observe(this, Observer {
            it?.let {
                random_beer_co.setup(it)
            }
        })

        viewModel.beerRx.observe(this, Observer {
            it?.let {
                random_beer_rx.setup(it)
            }
        })
    }

    private fun BeerCard.setup(beer: Beer) = this.setupView(
        BeerCard.Beer(
            name = beer.name,
            abv = beer.abv,
            ingredients = null,
            image_url = beer.labels?.first()?.icon
        )
    )
}
