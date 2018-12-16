package com.alexetnico.rxvscoroutines.ui.customview

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.alexetnico.rxvscoroutines.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.beer_card.view.*

class BeerCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    data class Beer(
        val name: String,
        val abv: String,
        val ingredients: String?,
        val image_url: String?
    )

    init {
        inflate(context, R.layout.beer_card, this)
    }

    fun setupView(beer: Beer) = with(beer) {

        beer_label.text = name
        beer_abv.text = abv
        beer_ingredients.text = ingredients
        when (image_url.isNullOrBlank()) {
            true -> beer_image.setImageDrawable(resources.getDrawable(R.drawable.default_success_beer, null))
            false -> Glide.with(context)
                .load(image_url)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .circleCrop()

                )
                .into(beer_image)
        }


    }
}