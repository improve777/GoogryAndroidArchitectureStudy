package dev.daeyeon.gaasproject.ui.ticker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.daeyeon.gaasproject.base.BaseCoroutineScope
import dev.daeyeon.gaasproject.base.BaseViewModel
import dev.daeyeon.gaasproject.data.StateResult
import dev.daeyeon.gaasproject.data.entity.Ticker
import dev.daeyeon.gaasproject.data.source.UpbitDataSource
import dev.daeyeon.gaasproject.util.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class TickerViewModel(
    private val markets: String,
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

    init {
        loadUpbitTicker()
    }

    fun loadUpbitTicker() {
        releaseCoroutine()

        job = SupervisorJob()

        viewModelScope.launch {
            withContext(Dispatchers.IO + job) {
                upbitRepository.getTicker(markets = markets)
                    .collect {
                        withContext(Dispatchers.Main + job) {
                            when (it) {
                                is StateResult.Success -> {
                                    _isShowProgressBar.value = false
                                    _tickerList.value = it.data
                                }
                                is StateResult.Error -> {
                                    _isShowProgressBar.value = false
                                    _failMsgEvent.value = Event(it.toString())
                                }
                                StateResult.Loading -> {
                                    _isShowProgressBar.value = true
                                }
                            }
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        Log.e("TickerViewModel", "onCleared()")
        super.onCleared()
    }
}