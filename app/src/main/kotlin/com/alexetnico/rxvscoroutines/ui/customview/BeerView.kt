package com.alexetnico.rxvscoroutines.ui.customview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.ImageView
import com.alexetnico.rxvscoroutines.R
import com.alexetnico.rxvscoroutines.utils.EMPTY
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.beer_view.view.*

class BeerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    data class Model(
        val name: String = EMPTY,
        val abv: String = EMPTY,
        val image_url: String? = null,
        val isLoading: Boolean = false
    )

    init {
        inflate(context, R.layout.beer_view, this)
        resources.getDimensionPixelSize(R.dimen.medium).let { setPadding(it, it, it, it) }
        setupFramework(attrs)
    }

    fun setupView(model: Model) = with(model) {
        beer_label.text = name
        beer_abv.text = "$abv%"
        beer_image.setupImage(image_url)
        beer_loader.visibility = if (isLoading) VISIBLE else GONE
    }

    private fun setupFramework(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.BeerView, 0, 0).apply {
                top_img.setImageResource(
                    when (getInteger(R.styleable.BeerView_framework, 0)) {
                        0 -> R.drawable.ic_rx
                        1 -> R.drawable.ic_co
                        else -> R.drawable.ic_co
                    }
                )
        }
    }

    private fun ImageView.setupImage(image_url: String?) {
        image_url?.let {
            Glide.with(context)
                .load(image_url)
                .apply(RequestOptions().centerCrop())
                .into(this)
        } ?: setImageResource(R.drawable.ic_android_beer)
    }

}