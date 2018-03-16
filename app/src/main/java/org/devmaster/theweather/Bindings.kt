package org.devmaster.theweather

import android.databinding.BindingAdapter
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.util.*

object Bindings {


    @JvmStatic
    @BindingAdapter("imageUrl")
    fun imageUrl(view: ImageView, url: String?) {
        if (url != null) {
            Glide.with(view.context).load(url).into(view)
        }
    }
    @JvmStatic
    @BindingAdapter("displayDate")
    fun displayDate(view: TextView, dt: Long?) {
        if (dt != null) {
            view.text = DateFormat.format("EEE, dd MMMM, yyyy", Date(dt))
        }
    }

}