package dev.daeyeon.gaasproject.module

import dev.daeyeon.gaasproject.ui.main.MainViewModel
import dev.daeyeon.gaasproject.ui.search.TickerSearchViewModel
import dev.daeyeon.gaasproject.ui.ticker.TickerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (markets: String) -> TickerViewModel(markets, get()) }

    viewModel { TickerSearchViewModel() }

    viewModel { MainViewModel(get()) }
}
