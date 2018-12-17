package com.alexetnico.rxvscoroutines.ui.customview

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.widget.ImageView
import com.alexetnico.rxvscoroutines.R
import com.alexetnico.rxvscoroutines.utils.EMPTY
import com.alexetnico.rxvscoroutines.utils.hide
import com.alexetnico.rxvscoroutines.utils.show
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.beer_view.view.*

class BeerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    data class Model(
        val name: String = EMPTY,
        val abv: String = EMPTY,
        val image_url: String? = EMPTY,
        val isLoading: Boolean = false
    )

    init {
        inflate(context, R.layout.beer_view, this)
    }

    fun setupView(model: Model) = with(model) {
        beer_label.text = name
        beer_abv.text = abv
        beer_image.setupImage(image_url)
        beer_loader.visibility = if (isLoading) VISIBLE else GONE
    }

    private fun ImageView.setupImage(image_url: String?) {
        val goodUrl = image_url ?: DEFAULT_IMG_URL
        if (goodUrl.isEmpty()) hide()
        else {
            Glide.with(context)
                .load(goodUrl)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .circleCrop()
                )
                .into(this)
            show()
        }
    }

    companion object {
        private const val DEFAULT_IMG_URL =
            "https://i.pinimg.com/originals/86/e4/37/86e437090aae1601525b1e6a8845dae2.jpg"
    }

}