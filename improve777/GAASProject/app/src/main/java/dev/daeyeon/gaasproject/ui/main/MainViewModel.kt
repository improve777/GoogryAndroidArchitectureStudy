package dev.daeyeon.gaasproject.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.daeyeon.gaasproject.base.BaseViewModel
import dev.daeyeon.gaasproject.data.StateResult
import dev.daeyeon.gaasproject.data.entity.Market
import dev.daeyeon.gaasproject.data.source.UpbitDataSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val upbitRepository: UpbitDataSource
) : BaseViewModel() {

    private val _marketList = MutableLiveData<List<Market>>()
    val marketList: LiveData<List<Market>> = _marketList

    init {
        getMarkets()
    }

    private fun getMarkets() {
        viewModelScope.launch {
            upbitRepository.getMarkets()
                .collect {
                    when (it) {
                        is StateResult.Success -> {
                            _marketList.value = it.data
                        }
                        is StateResult.Error -> {
                            Log.e("MainViewModel", it.exception.toString())
                        }
                        StateResult.Loading -> _isShowProgressBar.value = true
                    }
                }
        }
    }
}
