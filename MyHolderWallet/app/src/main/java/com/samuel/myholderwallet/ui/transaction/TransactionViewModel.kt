package com.samuel.myholderwallet.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.PaperRepository
import com.samuel.myholderwallet.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val paperRepository: PaperRepository,
    private val brokerRepository: BrokerRepository
) : ViewModel() {
    private val _allPapersEvent = MutableLiveData<List<PaperEntity>>()

    val allPapersEvent: LiveData<List<PaperEntity>>
        get() = _allPapersEvent

    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    fun getPapers() = viewModelScope.launch {
        _allPapersEvent.postValue(paperRepository.getAll())
    }

    fun getBrokers() = viewModelScope.launch {
        _allBrokersEvent.postValue(brokerRepository.getAll())
    }
}