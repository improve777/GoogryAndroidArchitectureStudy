package dev.daeyeon.gaasproject.ui.ticker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.daeyeon.gaasproject.base.BaseCoroutineScope
import dev.daeyeon.gaasproject.base.BaseViewModel
import dev.daeyeon.gaasproject.data.NetResult
import dev.daeyeon.gaasproject.data.Ticker
import dev.daeyeon.gaasproject.data.source.UpbitDataSource
import dev.daeyeon.gaasproject.util.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class TickerViewModel(
    private val upbitRepository: UpbitDataSource
) : BaseViewModel(), BaseCoroutineScope {

    override var job: Job = SupervisorJob()

    override fun releaseCoroutine() {
        Log.e("TickerViewModel", "releaseCoroutine")
        job.cancel()
    }

    private val _tickerList = MutableLiveData<List<Ticker>>(emptyList())
    val tickerList: LiveData<List<Ticker>> = _tickerList

    /**
     * 통신 실패시 메시지
     */
    private val _failMsgEvent = MutableLiveData<Event<String>>()
    val failMsgEvent: LiveData<Event<String>> = _failMsgEvent

    /**
     * 검색어 two-way binding
     */
    val searchText = MutableLiveData("")

    /**
     * 기준 마켓
     */
    private val _baseMarket = MutableLiveData(UpbitDataSource.ALL_MARKET)
    val baseMarket: LiveData<String> get() = _baseMarket

    init {
        loadUpbitTicker()
    }

    fun loadUpbitTicker() {
        releaseCoroutine()

        job = SupervisorJob()

        viewModelScope.launch {
            withContext(Dispatchers.IO + job) {
                upbitRepository.getTicker(
                    baseCurrency = _baseMarket.value ?: UpbitDataSource.ALL_MARKET,
                    searchTicker = searchText.value ?: UpbitDataSource.ALL_CURRENCY
                )
                    .collect {
                        withContext(Dispatchers.Main + job) {
                            when (it) {
                                is NetResult.Success -> {
                                    _isShowProgressBar.value = false
                                    _tickerList.value = it.data
                                }
                                is NetResult.Error -> {
                                    _isShowProgressBar.value = false
                                    _failMsgEvent.value = Event(it.toString())
                                }
                                NetResult.Loading -> {
                                    _isShowProgressBar.value = true
                                }
                            }
                        }
                    }
            }
        }
    }

    fun setBaseMarket(newBaseMarket: String) {
        _baseMarket.value =
            if (newBaseMarket == UpbitDataSource.ALL_CURRENCY) {
                ""
            } else {
                newBaseMarket
            }
    }

    fun getMarkets(): String = upbitRepository.markets


    override fun onCleared() {
        Log.e("TickerViewModel", "onCleared()")
        super.onCleared()
    }
}