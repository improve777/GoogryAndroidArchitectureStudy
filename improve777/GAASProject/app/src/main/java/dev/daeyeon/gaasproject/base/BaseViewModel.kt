package dev.daeyeon.gaasproject.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    /**
     * 프로그레스바
     */
    protected val _isShowProgressBar = MutableLiveData(false)
    val isShowProgressBar: LiveData<Boolean> = _isShowProgressBar
}