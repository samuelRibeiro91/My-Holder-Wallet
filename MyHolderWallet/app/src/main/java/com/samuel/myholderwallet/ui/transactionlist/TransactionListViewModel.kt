package com.samuel.myholderwallet.ui.transactionlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val transactionRepository: TransactionRepository,
    private val brokerRepository: BrokerRepository
) : ViewModel() {

    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    var brokerSelected = MutableLiveData<BrokerEntity>()

    private val _allTransactionsEvent = MutableLiveData<List<TransactionEntity>>()

    val allTransactionsEvent: LiveData<List<TransactionEntity>>
        get() = _allTransactionsEvent

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData : LiveData<Int>
        get() = _messageStateEventData

    fun getTransactions() = viewModelScope.launch {
        _allTransactionsEvent.postValue(transactionRepository.getAllByBroker(brokerSelected.value?.id ?: 0))
    }

    fun deleteTransaction(Transaction: TransactionEntity?) = viewModelScope.launch {
        try{
            Transaction?.let {
                transactionRepository.delete(Transaction)
                _messageStateEventData.value = R.string.transaction_deleted_sucessfully
            }

        } catch (ex: Exception){
            _messageStateEventData.value = R.string.transaction_error_to_delete
            Log.e(TAG, ex.toString())
        }
    }

    companion object{
        private val TAG = TransactionListViewModel::class.java.simpleName
    }

}