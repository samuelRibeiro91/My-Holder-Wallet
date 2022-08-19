package com.samuel.myholderwallet.ui.transactionlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.PaperRepository
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.usecases.TransactionCreditsValidateUseCase
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val transactionRepository: TransactionRepository,
    private val brokerRepository: BrokerRepository,
    private val paperRepository: PaperRepository,
    private val transactionCreditsValidateUseCase: TransactionCreditsValidateUseCase
) : ViewModel() {

    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    private val _allPapersEvent = MutableLiveData<List<PaperEntity>>()

    val allPapersEvent: LiveData<List<PaperEntity>>
      get() = _allPapersEvent

    var brokerSelected = MutableLiveData<BrokerEntity>()

    private val _allTransactionsEvent = MutableLiveData<List<TransactionEntity>>()

    val allTransactionsEvent: LiveData<List<TransactionEntity>>
        get() = _allTransactionsEvent

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData : LiveData<Int>
        get() = _messageStateEventData

    fun getBrokers() = viewModelScope.launch {
        _allBrokersEvent.postValue(brokerRepository.getAll())
    }

    fun getPapers() = viewModelScope.launch {
        _allPapersEvent.postValue(paperRepository.getAll())
    }

    fun getTransactions() = viewModelScope.launch {
        _allTransactionsEvent.postValue(transactionRepository.getAllByBroker(brokerSelected.value?.id ?: 0))
    }

    fun deleteTransaction(transaction: TransactionEntity?) = viewModelScope.launch {
        try{
            transaction?.let {
                transactionCreditsValidateUseCase.validateTransactionDelete(transaction)
                transactionRepository.delete(transaction)
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