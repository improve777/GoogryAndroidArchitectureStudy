package dev.daeyeon.gaasproject.ext

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import dev.daeyeon.gaasproject.data.entity.Market
import dev.daeyeon.gaasproject.ui.main.MainPagerAdapter

@BindingAdapter("replaceAll")
fun ViewPager2.replaceAll(list: List<*>?) {
    (adapter as? MainPagerAdapter)?.replaceAll(list as? List<Market> ?: emptyList())
}