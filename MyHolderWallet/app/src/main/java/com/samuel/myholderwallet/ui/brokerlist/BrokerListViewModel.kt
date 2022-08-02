package com.samuel.myholderwallet.ui.brokerlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.ui.broker.BrokerViewModel
import kotlinx.coroutines.launch

class BrokerListViewModel(private val repository: BrokerRepository): ViewModel() {
    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData : LiveData<Int>
        get() = _messageStateEventData

    fun getBrokers() = viewModelScope.launch {
        _allBrokersEvent.postValue(repository.getAll())
    }

    fun deleteBroker(broker: BrokerEntity?) = viewModelScope.launch {
        try{
            broker?.let {
                repository.delete(broker)
                _messageStateEventData.value = R.string.broker_deleted_sucessfully
            }

        } catch (ex: Exception){
            _messageStateEventData.value = R.string.broker_error_to_delete
            Log.e(TAG, ex.toString())
        }
    }

    companion object{
        private val TAG =BrokerListViewModel::class.java.simpleName
    }
}