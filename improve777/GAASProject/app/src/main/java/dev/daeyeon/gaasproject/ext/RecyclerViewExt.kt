package dev.daeyeon.gaasproject.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.daeyeon.gaasproject.data.entity.Ticker
import dev.daeyeon.gaasproject.ui.ticker.TickerAdapter

@BindingAdapter("items")
fun RecyclerView.setItems(items: List<Any>) {
    (adapter as? TickerAdapter)?.replaceList(items as? List<Ticker> ?: emptyList())
}