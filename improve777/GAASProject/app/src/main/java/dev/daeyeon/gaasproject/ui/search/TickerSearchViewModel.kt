package dev.daeyeon.gaasproject.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.daeyeon.gaasproject.base.BaseViewModel
import dev.daeyeon.gaasproject.util.Event

class TickerSearchViewModel : BaseViewModel() {

    private val _completeEvent = MutableLiveData<Event<Unit>>()
    val completeEvent: LiveData<Event<Unit>> = _completeEvent

    fun complete() {
        _completeEvent.value = Event(Unit)
    }
}