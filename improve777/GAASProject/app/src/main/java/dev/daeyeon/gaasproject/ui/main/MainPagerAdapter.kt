package dev.daeyeon.gaasproject.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.daeyeon.gaasproject.data.entity.Market
import dev.daeyeon.gaasproject.ui.ticker.TickerFragment

class MainPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val items = mutableListOf<Market>()

    fun replaceAll(list: List<Market>?) {
        list?.let {
            items.clear()
            items.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun getPageTitle(position: Int) = items[position].currency

    override fun getItem(position: Int): Fragment = TickerFragment.newInstance(items[position])

    override fun getItemCount(): Int = items.size
}