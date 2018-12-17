package com.alexetnico.rxvscoroutines.ui.customview

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.alexetnico.rxvscoroutines.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.beer_card.view.*

class BeerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    data class Model(
        val name: String,
        val abv: String,
        val image_url: String?
    )

    init {
        inflate(context, R.layout.beer_card, this)
    }

    fun setupView(model: Model) = with(model) {
        beer_label.text = name
        beer_abv.text = abv
        Glide.with(context)
            .load(image_url ?: DEFAULT_IMG_URL)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .circleCrop()
            )
            .into(beer_image)
    }


    companion object {
        private const val DEFAULT_IMG_URL =
            "https://i.pinimg.com/originals/86/e4/37/86e437090aae1601525b1e6a8845dae2.jpg"
    }

}