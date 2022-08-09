package com.samuel.myholderwallet.ui.broker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import kotlinx.coroutines.launch

class BrokerViewModel(
    private val repository: BrokerRepository
) : ViewModel() {

    private val _brokerStateEventData = MutableLiveData<BrokerState>()
    val brokerStateEventData : LiveData<BrokerState>
        get() = _brokerStateEventData

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData : LiveData<Int>
        get() = _messageStateEventData

    fun insertOrUpdateBroker(name: String, id: Long = 0) = viewModelScope.launch {
        if (id > 0){
            updateBroker(id, name)
        }
        else
        {
            insertBroker(name)
        }
    }

    private fun updateBroker(id: Long, name: String) = viewModelScope.launch {
        try {
            repository.update(BrokerEntity().apply { this.id   = id
                                                     this.name = name })

            _brokerStateEventData.value = BrokerState.Updated
            _messageStateEventData.value = R.string.broker_updated_sucessfully

        } catch (ex: Exception){
            _messageStateEventData.value = R.string.broker_error_to_update
            Log.e(TAG, ex.toString())
        }
    }

    private fun insertBroker(name: String) = viewModelScope.launch {
        try {
            val id = repository.insert(BrokerEntity().apply { this.name = name } )

            if (id > 0){
                _brokerStateEventData.value = BrokerState.Inserted
                _messageStateEventData.value = R.string.broker_inserted_sucessfully
            }

        } catch (ex: Exception){
            _messageStateEventData.value = R.string.broker_error_to_insert
            Log.e(TAG, ex.toString())
        }
    }



    sealed class BrokerState{
        object Inserted: BrokerState()
        object Updated: BrokerState()
    }

    companion object{
        private val TAG =BrokerViewModel::class.java.simpleName
    }
}