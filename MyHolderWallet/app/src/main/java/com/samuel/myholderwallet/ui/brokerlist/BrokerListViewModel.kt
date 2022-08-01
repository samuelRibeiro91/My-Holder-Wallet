package com.samuel.myholderwallet.ui.brokerlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import kotlinx.coroutines.launch

class BrokerListViewModel(private val repository: BrokerRepository): ViewModel() {
    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    fun getBrokers() = viewModelScope.launch {
        _allBrokersEvent.postValue(repository.getAll())
    }
}